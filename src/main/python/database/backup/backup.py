import os, sys
import time



def get_latest_file_dir(dir_path):
    files = os.listdir(dir_path)
    paths = [os.path.join(dir_path, basename) for basename in files]
    return max(paths, key=os.path.getctime)


class Backup:
    def __init__(self, config):
        self._config = config
        os.chdir("C:/Program Files/MySQL/MySQL Server 8.0/bin")

    def recovery(self):
        file_path = get_latest_file_dir(self._config.get("DATABASE.BACKUP.BACKUP_DIR")).replace("/", "\\")

        recovery_cmd = f'gzip -d {file_path} | mysql -h {self._config.get("DATABASE.DB_HOST")} -u {self._config.get("DATABASE.DB_USER")} -p{self._config.get("DATABASE.DB_PASS")} {self._config.get("DATABASE.DB_NAME")}'
        os.system(recovery_cmd)
        gzip_cmd = f'gzip {file_path.split(".gz")[0]}'
        os.system(gzip_cmd)

    def backup(self):
        date_format = '%Y-%m-%d_%H-%M-%S'
        while True:
            current_time = time.strftime(date_format)
            backup_file = f'{self._config.get("DATABASE.DB_NAME")}-{current_time}.sql'
            backup_file_path = os.path.join(self._config.get("DATABASE.BACKUP.BACKUP_DIR"), backup_file)

            mysqldump_cmd = f'mysqldump -h {self._config.get("DATABASE.DB_HOST")} -u {self._config.get("DATABASE.DB_USER")} -p{self._config.get("DATABASE.DB_PASS")} {self._config.get("DATABASE.DB_NAME")} > {backup_file_path}'
            os.system(mysqldump_cmd)

            gzip_cmd = f'gzip {backup_file_path}'
            os.system(gzip_cmd)
            # find_cmd = f'find {BACKUP_DIR} -type f -name "*.gz" -mtime +7 -delete'
            # os.system(find_cmd)
            time.sleep(self._config.get("DATABASE.BACKUP.BACKUP_INTERVAL"))
