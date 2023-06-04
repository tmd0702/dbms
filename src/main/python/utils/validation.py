import re


def validate_password(password):
    # min length is 8 and max length is 25
    # at least include a digit number,
    # at least an upcase and a lowcase letter
    # at least a special characters
    return re.match("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,25}$", password)


def validate_username(username):
    # Username consists of alphanumeric characters (a-zA-Z0-9), lowercase, or uppercase.
    # Username allowed of the dot (.), underscore (_), and hyphen (-).
    # The dot (.), underscore (_), or hyphen (-) must not be the first or last character.
    # The dot (.), underscore (_), or hyphen (-) does not appear consecutively, e.g., java..regex
    # The number of characters must be between 5 and 25.
    return re.match("^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$", username)


def validate_email(email):
    # This regular expression is provided by the OWASP validation regex repository to check the email validation:
    return re.match("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$", email)
