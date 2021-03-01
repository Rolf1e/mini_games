use crate::case::Case;
use crate::error::Error;
use crate::turn::Turn;

#[derive(Debug)]
pub enum Board {
    ConnectFour(Vec<Vec<Case>>),
}

pub type Coordinate = (usize, usize);

impl Board {
    pub fn display(&self) -> String {
        match self {
            Board::ConnectFour(board) => {
                let mut display = String::new();
                let mut board = board.to_owned();
                board.reverse();
                for row in board {
                    display.push('|');
                    for case in row {
                        display.push_str(format!(" {} |", case.display()).as_str());
                    }
                    display.push('\n');
                }
                display
            }
        }
    }

    pub fn place(&mut self, case: Case, x: usize, y: usize) -> Result<(), Error> {
        match self {
            Board::ConnectFour(board) => {
                board[y][x] = case;
                Ok(())
            }
        }
    }

    pub fn at(&self, (x, y): Coordinate) -> &Case {
        match self {
            Board::ConnectFour(board) => &board[y][x],
        }
    }

    pub fn play(&mut self, turn: Turn) -> Result<(), Error> {
        match (self, turn) {
            (Board::ConnectFour(board), Turn::ConnectFour(column)) => {
                play_at_connect_four(board, column)
            }
        }
    }
}

fn play_at_connect_four(board: &mut Vec<Vec<Case>>, column: usize) -> Result<(), Error> {
    let length = board.len() - 1;
    if column > length {
        Err(Error::IllegalMove(String::from("Column is out of bounds")))
    } else {
        todo!()
    }
}

