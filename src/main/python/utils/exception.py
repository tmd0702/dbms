class EmailValidationError(Exception):
    def __init__(self, message, errors):
        super().__init__(message)
        self.errors = errors


class UsernameValidationError(Exception):
    def __init__(self, message, errors):
        super().__init__(message)
        self.errors = errors


class PasswordValidationError(Exception):
    def __init__(self, message, errors):
        super().__init__(message)
        self.errors = errors