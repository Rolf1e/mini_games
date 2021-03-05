
#[derive(Debug, Clone, PartialEq, Eq)]
pub enum Piece {
    ConnectFour(String, Rank),
}

#[derive(Debug, Copy, Clone, PartialEq, Eq)]
pub enum Rank {
    ConnectFour,
}

impl Piece {
    pub fn display(&self) -> String {
        match self {
            Piece::ConnectFour(color, _) => format!("{}", color),
        }
    }
    
    pub fn rank(&self) -> &Rank {
        match self {
            Piece::ConnectFour(_, rank) => rank,
        }
    }
}


impl Rank {
    pub fn get(&self) -> usize  {
        match self {
            Rank::ConnectFour => 0,
        }
    }
}

