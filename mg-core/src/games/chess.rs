use crate::error::MGError;
use crate::player::Action;


#[derive(Debug)]
pub enum ChessException {
    Extraction(String),
}

impl ChessException {
    pub fn to_error(&self) -> MGError {
        match self {
            ChessException::Extraction(e) => MGError::Infra(e.to_owned()),
        }
    }
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ChessState {
    Red,
    Yellow,
    Over(Option<ChessColor>),
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ChessColor {
    Red,
    Yellow,
    Equality,
}

/// input format -> f1,e1
pub fn parse_input(input: String) -> Result<Action, MGError> {
    let moves: Vec<_> = input.split(',')
        .collect();
    if 2 != moves.len() {
        return Err(MGError::ParseInputAction(input));
    } 

    Ok(Action::Chess(parse_one_coord(moves[0])?, parse_one_coord(moves[1])?))
}

fn parse_one_coord(input: &str) -> Result<(String, usize), MGError> {
    let splited_input: Vec<_> = input.chars().collect();
    if 2 != input.len() {
        return Err(MGError::ParseInputAction(input.to_string()));
    } 

    if let Some(parsed) = splited_input[1].to_digit(10) {
        Ok((splited_input[0].to_string(), parsed as usize))
    } else {
        Err(MGError::ParseInputAction(input.to_string()))
    }
}

#[test]
fn should_parse_input() -> Result<(), MGError> {
    let action = Action::Chess((String::from("f"), 1), (String::from("e"), 1));
    assert_eq!(action, parse_input(String::from("f1,e1"))?);

    let action = Action::Chess((String::from("a"), 1), (String::from("b"), 6));
    assert_eq!(action, parse_input(String::from("a1,b6"))?);

    let action = Action::Chess((String::from("f"), 1), (String::from("e"), 1));
    assert_eq!(action, parse_input(String::from("f1,e1"))?);
    Ok(())
}

// https://www.chessprogramming.org/Bitboards
// http://pages.cs.wisc.edu/~psilord/blog/data/chess-pages/rep.html
pub fn check_has_win() {
    todo!()
}
