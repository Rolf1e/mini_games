#[derive(Debug, Eq, PartialEq)]
pub enum Error {
    WrongCaseType,
    IllegalMove(String),
    BadGame,
}

impl Error {
    pub fn message(&self) -> String {
        match self {
            Error::WrongCaseType => String::from("This case type does not exist"),
            Error::IllegalMove(s) => s.to_owned(),
            Error::BadGame => String::from("Game does not exist"),
        }
    }
}
