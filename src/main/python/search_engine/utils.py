import spacy
import nltk
from nltk.tokenize import word_tokenize, sent_tokenize
from nltk.corpus import stopwords
import regex as re
import string
import numpy as np
from numpy.linalg import norm
import re


def convert_to_lower(text):
    return text.lower()


def remove_numbers(text):
    text = re.sub(r'd+', '', text)
    return text


def remove_http(text):
    text = re.sub("https?://t.co/[A-Za-z0-9]*", ' ', text)
    return text


def remove_short_words(text):
    text = re.sub(r'bw{1,2}b', '', text)
    return text


def remove_punctuation(text):
    punctuations = '''!()[]{};«№»:'",`./?@=#$-(%^)+&[*_]~'''
    no_punctuation = ""
    for char in text:
        if char not in punctuations:
            no_punctuation = no_punctuation + char
    return no_punctuation


def remove_white_space(text):
    text = text.strip()
    return text


def toknizing(text, remove_stop_words):
    stop_words = set(stopwords.words('english'))
    tokens = word_tokenize(text)
    ## Remove Stopwords from tokens
    if remove_stop_words:
        result = [i for i in tokens if not i in stop_words]
    else:
        result = tokens
    return result


def preprocessing(text, remove_stop_words=True):
    return toknizing(remove_white_space(
        remove_punctuation(remove_short_words(remove_http(convert_to_lower(text))))), remove_stop_words)


def cosine_similarity(v1, v2):
    cosine = np.dot(v1,v2)/(norm(v1)*norm(v2))
    return cosine
