from flask import Flask, request, jsonify
from flask_cors import CORS, cross_origin
from http import HTTPStatus
import os, sys
import numpy as np
import asyncio
import multiprocessing
import json
import pandas as pd
from config import Config
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/database/processor/")
from processor_manager import ProcessorManager
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/database/backup/")
from backup import Backup
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/utils/")
from others import *
from datatype import *
from validation import *
from exception import *
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/search_engine/")
from search_engine import *


app = Flask(__name__)

CORS(app, resources={r"/*": {"origins": "*"}})
app.config['CORS_HEADERS'] = 'Content-Type'

config = Config()

processor_manager = ProcessorManager(config)
search_engine = SearchEngine(config)
backup = Backup(config)

def get_processor_by_name(norm_processor_name):
    if norm_processor_name == "user":
        return  processor_manager.get_account_management_processor()
    elif norm_processor_name == "authentication":
        return processor_manager.get_authentication_management_processor()
    elif norm_processor_name == "booking_item":
        return processor_manager.get_booking_item_management_processor()
    elif norm_processor_name == "booking_ticket":
        return processor_manager.get_booking_ticket_management_processor()
    elif norm_processor_name == "cinema":
        return processor_manager.get_cinema_management_processor()
    elif norm_processor_name == "item":
        return processor_manager.get_item_management_processor()
    elif norm_processor_name == "payment":
        return processor_manager.get_payment_management_processor()
    elif norm_processor_name == "revie":
        return processor_manager.get_review_management_processor()
    elif norm_processor_name == "schedule":
        return processor_manager.get_schedule_management_processor()
    elif norm_processor_name == "screen_room":
        return processor_manager.get_screen_room_management_processor()
    elif norm_processor_name == "show_time":
        return processor_manager.get_show_time_management_processor()
    elif norm_processor_name == "movie":
        return processor_manager.get_movie_management_processor()
    elif norm_processor_name == "promotion":
        return processor_manager.get_promotion_management_processor()


@app.route("/auth/sign_up", methods=['POST'])
def sign_up():
    request_data = json.loads((request.stream.read().decode('utf-8')))
    user_information = {key:request_data[key] for key in request_data if key != 'pass'}
    auth_information = {key:request_data[key] for key in request_data if key == 'pass'}
    status_code, error_message, data = HTTPStatus.OK, None, None
    try:
        if not validate_username(user_information["username"]):
            raise UsernameValidationError("invalid username")
        if not validate_email(user_information["email"]):
            raise EmailValidationError("invalid email")
        if not validate_password(auth_information["pass"]):
            raise PasswordValidationError("invalid password")
        data = processor_manager.get_database().get_cursor().callproc("signUpProcedure", args=(processor_manager.get_id_generation().generate_id("USERS"), processor_manager.get_id_generation().generate_id("AUTHENTICATION"), user_information["username"], user_information["first_name"], auth_information["pass"], user_information["last_name"], user_information["dob"], user_information["gender"], user_information["address"], user_information["phone"], user_information["email"]))
    except Exception as e:
        status_code = HTTPStatus.BAD_REQUEST
        error_message = str(e)

    return json.dumps({
        "status_code": status_code,
        "error_message": error_message,
        "data": data
    })


@app.route("/auth/sign_in", methods=['POST'])
def sign_in():
    request_data = json.loads((request.stream.read().decode('utf-8')))
    username = request_data.get("username")
    password = request_data.get("password")
    status_code, error_message = HTTPStatus.OK, None

    response = processor_manager.get_authentication_management_processor().select_data(0, 1, f"U.USERNAME = '{username}'", "")
    if response["status_code"] == HTTPStatus.OK:
        if len(response["data"]["values"]) and password == get_data_row_value_by_column_name(response["data"], "PASSWORD", 0):
            error_message = "sign in success"
        else:
            status_code = HTTPStatus.BAD_REQUEST
            error_message = "Wrong username or password"
    return json.dumps({
        "status_code": status_code,
        "error_message": error_message
    })


@app.route("/database/commit", methods=["POST"])
def database_commit():
    status_code = HTTPStatus.OK
    error_message = None
    try:
        processor_manager.get_database().commit()
    except Exception as e:
        error_message = str(e)
        status_code = HTTPStatus.BAD_REQUEST

    return {
        "error_message": error_message,
        "status_code": status_code
    }


@app.route("/database/rollback", methods=["POST"])
def database_rollback():
    status_code = HTTPStatus.OK
    error_message = None
    try:
        processor_manager.get_database().rollback()
    except Exception as e:
        error_message = str(e)
        status_code = HTTPStatus.BAD_REQUEST

    return {
        "error_message": error_message,
        "status_code": status_code
    }


@app.route("/database/proc/dql/select", methods=["GET"])
def select():
    processor = get_processor_by_name(request.args.get('processor')[:-1].lower())
    return processor.select_data(int(request.args.get('start_index')), int(request.args.get('quantity')), request.args.get('query_condition'), request.args.get('sort_query'), request.args.get('count') == 'true')


@app.route("/database/proc/dml/insert", methods=["POST"])
def insert():
    request_data = json.loads((request.stream.read().decode('utf-8')))
    processor = get_processor_by_name(request_data.get('processor')[:-1].lower())
    return processor.insert_data(request_data.get('column_value_dict'), request_data.get('is_commit') == 'true')


@app.route("/database/proc/dml/update", methods=["PUT"])
def update():
    request_data = json.loads((request.stream.read().decode('utf-8')))
    processor = get_processor_by_name(request_data.get('processor')[:-1].lower())
    return processor.update_data(request_data.get('column_value_dict'), request_data.get('query_condition'), request_data.get('is_commit') == 'true')


@app.route("/database/proc/dml/delete", methods=["DELETE"])
def delete():
    processor = get_processor_by_name(request.args.get('processor')[:-1].lower())
    return processor.delete_data(request.args.get('query_condition'), request.args.get('is_commit') == 'true')


@app.route("/search_engine/keyword_searching", methods=["GET"])
def keyword_searching():
    return search_engine.keyword_searching(request.args.get('input'))


@app.route("/search_engine/semantic_searching", methods=["GET"])
def semantic_searching():
    return search_engine.semantic_searching(request.args.get('input'))


@app.route("/search_engine/combination_searching", methods=["GET"])
def combination_searching():
    return search_engine.combination_searching(request.args.get('input'))


def run_server():
    app.run(debug=False, port=config.get("API.API_PORT"), host=config.get("API.API_HOST"))


run_server()
# backup.backup()
