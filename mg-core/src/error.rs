#[derive(Debug, Eq, PartialEq)]
pub enum MGError {
    WrongCaseType,
    IllegalMove(String),
    BadGame,
    Infra(String),
    ReadInputAction,
    ParseInputAction(String),
}

impl MGError {
    pub fn message(&self) -> String {
        match self {
            MGError::WrongCaseType => String::from("This case type does not exist"),
            MGError::IllegalMove(s) => s.to_owned(),
            MGError::BadGame => String::from("Game does not exist"),
            MGError::Infra(e) => e.to_owned(),
            MGError::ReadInputAction => String::from("Failed to read input move"),
            MGError::ParseInputAction(input) => format!("Failed to parse input {}", input),
        }
    }
}
