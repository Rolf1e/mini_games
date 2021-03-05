use crate::case::Case;
use crate::error::Error;
use crate::piece::{Piece, Rank};
use crate::turn::Action;

#[derive(Debug, Clone)]
pub enum Board {
    ConnectFour(Vec<Vec<Case>>),
}

pub type Coordinate = (usize, usize);

impl Board {
    pub fn play(&mut self, action: Action) -> Result<(), Error> {
        match (&self, action) {
            (Board::ConnectFour(_), Action::ConnectFour(column_index, color)) => {
                play_at_connect_four(column_index, color, self)
            }
        }
    }

    pub fn at(&self, (x, y): Coordinate) -> &Case {
        match self {
            Board::ConnectFour(board) => &board[x][y],
        }
    }

    pub fn display(&self) -> String {
        match self {
            Board::ConnectFour(board) => {
                let mut display = String::new();
                let board = board.to_owned();
                let l = board.len();
                for y in (0..l).rev() {
                    display.push('|');
                    for x in 0..l {
                        let case = self.at((x, y));
                        display.push_str(format!(" {} |", case.display()).as_str());
                    }
                    display.push('\n');
                }
                display
            }
        }
    }

    fn place(&mut self, case: Case, x: usize, y: usize) -> Result<(), Error> {
        match self {
            Board::ConnectFour(board) => {
                board[x][y] = case;
                Ok(())
            }
        }
    }
}

fn play_at_connect_four(
    column_index: usize,
    color: String,
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

fn index_of_new_pon(column: &[Case]) -> Option<usize> {
    for (i, case) in column.iter().enumerate() {
        if &Case::Empty == case {
            return Some(i);
        }
    }
    None
}
