
#[derive(Debug, Clone)]
pub enum Piece {
    ConnectFour(String, Rank),
}

#[derive(Debug, Clone)]
pub enum Rank {
    ConnectFour(bool),
}

impl Piece {
    pub fn display(&self) -> String {
        match self {
            Piece::ConnectFour(color, rank) => format!("{}{}", color, rank.get()),
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
            Rank::ConnectFour(value) => value.clone() as usize,
        }
    }
}

