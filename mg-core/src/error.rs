#[derive(Debug)]
pub enum Error {
    WrongCaseType,
    IllegalMove(String),
}

impl Error {
    pub fn message(&self) -> String {
        match self {
            Error::WrongCaseType => String::from("This case type does not exist"),
            Error::IllegalMove(s) => s.to_owned(),
        }
    }
}
