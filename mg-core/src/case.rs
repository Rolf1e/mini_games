use crate::piece::Piece;

#[derive(Debug, Clone)]
pub enum Case {
    Empty,
    ConnectFour(Piece),
}

impl Case {
    pub fn display(&self) -> String {
        match self {
            Case::Empty => String::from("    "),
            Case::ConnectFour(piece) => format!("{}", piece.display()),
        }
    }

    pub fn get_content(&self) -> Option<&Piece> {
        match self {
            Case::Empty => None,
            Case::ConnectFour(piece) => Some(piece),
        }
    }
}
