from sentence_transformers import SentenceTransformer
import pandas as pd
import utils
import numpy as np
import json


class SearchEngine:
    def __init__(self, config):
        self._config = config
        self.keyword_list = None
        self.id_list = None
        self.model = SentenceTransformer(self._config.get("SEARCH_ENGINE.LANGUAGE_MODEL"))
        self.keywords_dict = dict()
        self.embedding_dict = dict()
        self.keywords_id_map = dict()
        self.movies_data = pd.read_csv("../resources/4hb-data/movies_metadata_cleaned.csv")
        self.keywords_df = None

        self._keywords_df_init()
        self._keywords_id_map_init()
        self._embedding_dict_init()
        self._keywords_dict_init()

    def _keywords_id_map_init(self):
        def map_func_get_keyword(x):
            return x['name']

        for keywords, id in zip(self.keyword_list, self.id_list):
            try:
                self.keywords_id_map[id] = list(map(map_func_get_keyword, json.loads(keywords)))
            except Exception as e:
                print(e)

    def _keywords_df_init(self):
        self.keywords_df = pd.read_csv("../resources/4hb-data/keywords.csv")
        for index, row in self.keywords_df.iterrows():
            try:
                self.keywords_df.loc[index, 'keywords'] = row.iloc[1].replace("{'", '{"').replace("':", '":').replace(
                    " '", ' "').replace("'}", '"}').replace('\\', '/')
            except Exception as e:
                print(e)

        self.id_list = self.keywords_df.id.values
        self.keyword_list = self.keywords_df.keywords.values


    def _keywords_dict_init(self):
        for id in self.movies_data.id.values:
            try:
                keywords = list(
                    set(np.append(self.keywords_id_map[int(id)], np.array(
                        [utils.preprocessing(self.movies_data.loc[self.movies_data.id == int(id), 'title'].iloc[0])]))))
                self.keywords_dict[int(id)] = keywords
            except:
                print(id, 'not found')
                continue

    def _embedding_dict_init(self):
        for id in self.movies_data.id.values:
            movie_overview = self.movies_data.loc[self.movies_data.id == int(id), 'overview'].values[0]
            overview_embedding = self.model.encode(movie_overview)
            self.embedding_dict[int(id)] = overview_embedding

    def keyword_searching(self, input):
        norm_input = utils.preprocessing(input)

        scores = dict()
        for id in self.movies_data.id.values:
            keywords = self.keywords_dict.get(int(id))
            n = len(keywords)
            if n == 0:
                continue
            count = 0
            for keyword in keywords:
                count += int(keyword in norm_input)
            scores[int(id)] = count / n
        sorted_scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
        return json.dumps(sorted_scores)

    def semantic_searching(self, input):
        scores = {}
        input_embedding = self.model.encode(input)

        for id in self.movies_data.id.values:
            overview_embedding = self.embedding_dict.get(int(id))
            scores[int(id)] = (np.float64(utils.cosine_similarity(input_embedding, overview_embedding)) - 0.4) / 0.6
        sorted_scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True) if v > 0}
        return json.dumps(sorted_scores)

    def combination_searching(self, input):
        scores = {}
        norm_input = utils.preprocessing(input)
        input_embedding = self.model.encode(input)

        for id in self.movies_data.id.values:
            overview_embedding = self.embedding_dict.get(int(id))
            semantic_score = (np.float64(utils.cosine_similarity(input_embedding, overview_embedding)) - 0.4) / 0.6

            keywords = self.keywords_dict.get(int(id))
            n = len(keywords)
            if n == 0:
                keyword_score = 0
            else:
                count = 0
                for keyword in keywords:
                    count += int(keyword in norm_input)
                keyword_score = (count / n - 0.0) / 1
            score = max(semantic_score, keyword_score)
            if score > 0:
                scores[int(id)] = score
        sorted_scores = {k: v for k, v in sorted(scores.items(), key=lambda item: item[1], reverse=True)}
        return json.dumps(sorted_scores)
