use crate::games::connect_four::ConnectFourColor;

#[derive(Debug, Clone, PartialEq, Eq)]
pub enum Piece {
    ConnectFour(ConnectFourColor, Rank),
}

#[derive(Debug, Copy, Clone, PartialEq, Eq)]
pub enum Rank {
    ConnectFour,
}

impl Piece {
    pub fn display(&self) -> String {
        match self {
            Piece::ConnectFour(color, _) => color.to_string(),
        }
    }

    pub fn rank(&self) -> &Rank {
        match self {
            Piece::ConnectFour(_, rank) => rank,
        }
    }
}

impl Rank {
    pub fn get(&self) -> usize {
        match self {
            Rank::ConnectFour => 0,
        }
    }
}
