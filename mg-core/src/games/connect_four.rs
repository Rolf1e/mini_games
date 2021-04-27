use crate::board::Board;
use crate::case::Case;
use crate::error::Error;
use crate::piece::{Piece, Rank};
use crate::state::State;
use std::fmt;

#[derive(Debug)]
pub enum ConnectFourException {
    Extraction(String),
}

impl ConnectFourException {
    pub fn to_error(&self) -> Error {
        match self {
            ConnectFourException::Extraction(e) => Error::Infra(e.to_owned()),
        }
    }
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ConnectFourState {
    Red,
    Yellow,
    Over(Option<ConnectFourColor>),
}

#[derive(Debug, Eq, PartialEq, Clone)]
pub enum ConnectFourColor {
    Red,
    Yellow,
    Equality,
}

pub fn play_at_connect_four(
    column_index: usize,
    color: ConnectFourColor,
    board: &mut Board,
) -> Result<(), Error> {
    let Board::ConnectFour(cases) = board;
    if let Some(column) = cases.get(column_index) {
        if let Some(row_index) = index_of_new_pon(&column) {
            board.place(
                Case::ConnectFour(Piece::ConnectFour(color, Rank::ConnectFour)),
                column_index,
                row_index,
            )?;
            Ok(())
        } else {
            Err(Error::IllegalMove(String::from("Column is full")))
        }
    } else {
        Err(Error::IllegalMove(format!(
            "Column {} is out of bounds",
            column_index
        )))
    }
}

/// To check game state
/// Row is Board[x][0] -> we fix 0
/// Column is Board[0][y] -> we fix 0
pub fn check_is_over(board: &Board, pon: &i8) -> Result<Option<ConnectFourColor>, Error> {
    if let Some(equality) = check_board_is_full(board) {
        return Ok(Some(equality));
    }

    match (
        check_has_win(&board, pon, &ConnectFourColor::Red),
        check_has_win(&board, pon, &ConnectFourColor::Yellow),
    ) {
        (Ok(false), Ok(false)) => Ok(None),
        (Ok(true), _) => {eprintln!("HERE"); Ok(Some(ConnectFourColor::Red))},
        (_, Ok(true)) => {eprintln!("HERE"); Ok(Some(ConnectFourColor::Yellow))},
        (Err(e), _) | (_, Err(e)) => Err(e.to_error()),
    }
}

fn check_has_win(
    board: &Board,
    pon: &i8,
    color: &ConnectFourColor,
) -> Result<bool, ConnectFourException> {
    eprintln!("{}", board.display());
    let (cases, last_column_index, last_row_index) = transform_to_i16(board, color)?;
    eprintln!("{} ->{:?}", color, cases);
    if check_row(&cases, &last_row_index, pon) {
        Ok(true)
    } else if check_column(&cases, &last_column_index, pon) {
        Ok(true)
    } else {
        Ok(false)
    }
}

fn check_row(cases: &[i16], last_row_index: &i8, pon: &i8) -> bool {
    let mut stack = 0;
    let mut row = 0;
    let mut column = 0;
    let length = cases.len() as i8;
    let mut parcoured = 0;
    while parcoured < length {
        if pon == &stack {
            return true;
        } 

        column += last_row_index;
        if 0 != parcoured && 0 == column % length {
            row += 1;
            stack = 0;
            column = 0;
        } 

        let index = row + column;
        if 1 == cases[index as usize] {
            stack += 1;
        } else {
            stack = 0;
        }

        parcoured += 1;
    }

    false
}

fn check_column(cases: &[i16], last_column_index: &i8, pon: &i8) -> bool {
    let mut stack = 0;
    let mut index = 0;
    for case in cases {
        if pon == &stack {
            return true;
        } else if 0 != index && 0 == index % last_column_index {
            stack = 0;
        } 

        if &1 == case {
            stack += 1;
        } else {
            stack = 0;
        }

        index += 1;
    }
    false
}

fn transform_to_i16(
    board: &Board,
    color: &ConnectFourColor,
) -> Result<(Vec<i16>, i8, i8), ConnectFourException> {
    let Board::ConnectFour(cases) = board;
    let last_column_index = cases.len();
    let mut new_cases = Vec::new();
    let last_row_index = cases[0].len();
    for row in cases.iter() {
        for case in row {
            new_cases.push(if &Case::Empty == case { 
                0 
            } else if is_color(&get_piece(case)?, &color) {
                1
            } else {
                0
            });
        }
    }
    Ok((new_cases, last_column_index as i8, last_row_index as i8))
}

fn get_piece(case: &Case) -> Result<Piece, ConnectFourException> {
    if let Some(piece) = case.get_content() {
        Ok(piece.clone())
    } else {
        Err(ConnectFourException::Extraction(format!(
            "Failed to extract content for case, {}",
            case.display()
        )))
    }
}

fn is_color(piece: &Piece, color: &ConnectFourColor) -> bool {
    let Piece::ConnectFour(actual_color, _) = piece;
    actual_color == color
}

fn check_board_is_full(board: &Board) -> Option<ConnectFourColor> {
    let Board::ConnectFour(cases) = board;
    let max_row = cases.len();
    let max_column = cases[0].len() - 1;
    for i in 0..max_row {
        if Case::Empty == cases[i][max_column] {
            return None;
        }
        eprintln!("C:{:?}", cases[i][max_column]);
    }
    Some(ConnectFourColor::Equality)
}

impl State for ConnectFourState {
    fn next(&mut self, board: &Board) -> Result<(), Error> {
        *self = match self {
            ConnectFourState::Red => {
                if let Some(color) = check_is_over(board, &4)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Yellow
                }
            }
            ConnectFourState::Yellow => {
                if let Some(color) = check_is_over(board, &4)? {
                    ConnectFourState::Over(Some(color))
                } else {
                    ConnectFourState::Red
                }
            }
            ConnectFourState::Over(_) => self.clone(),
        };
        Ok(())
    }

    fn message(&self) -> String {
        match &self {
            ConnectFourState::Red => String::from("Red is playing"),
            ConnectFourState::Yellow => String::from("Yellow is playing"),
            ConnectFourState::Over(color) => {
                if let Some(color) = color {
                    format!("Game is over, {} has win !", color)
                } else {
                    String::from("Not over yet")
                }
            }
        }
    }
}

impl fmt::Display for ConnectFourColor {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        let s = match self {
            ConnectFourColor::Red => "Red",
            ConnectFourColor::Yellow => "Yel",
            ConnectFourColor::Equality => "NoO",
        };
        write!(f, "{}", s)
    }
}

pub fn display(board: &Board) -> String {
    let Board::ConnectFour(cases) = board;
    let cases_length = cases.len();
    let mut message = String::new();
    for y in (0..cases_length).rev() {
        message.push('|');
        for x in 0..cases_length {
            let case = board.at((x, y));
            message.push_str(format!(" {} |", case.display()).as_str());
        }
        message.push('\n');
    }
    message
}

fn index_of_new_pon(column: &[Case]) -> Option<usize> {
    for (i, case) in column.iter().enumerate() {
        if &Case::Empty == case {
            return Some(i);
        }
    }
    None
}
