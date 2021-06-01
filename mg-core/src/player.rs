use crate::board::Board;
use crate::error::MGError;
use crate::games::connect_four::ConnectFourColor;
use std::io;
use std::str;

pub trait Player {
    fn ask_next_move(&self, board: &Board) -> Result<Action, MGError>;

    fn get_color(&self) -> &str;

    fn get_name(&self) -> &str;
}

pub enum Action {
    ConnectFour(usize, ConnectFourColor),
}

#[derive(Debug)]
pub enum Color {
    ConnectFour(ConnectFourColor),
}

pub struct ConsolePlayer {
    color: Color,
    name: String,
}

impl Player for ConsolePlayer {

    fn ask_next_move(&self, _board: &Board) -> Result<Action, MGError> {
        println!("{:?} is playing", self.color);
        println!("from ? (e.g 0A)");
        if let Ok(input) = read_from_input() {
            Ok(Board::parse_input(input, &self.color)?)
        } else {
            Err(MGError::ReadInputAction)
        }
    }

    fn get_color(&self) -> &str {
        todo!()
    }

    fn get_name(&self) -> &str {
        &self.name
    }
}

impl ConsolePlayer {
    pub fn new(color: Color, name: String) -> Self {
        ConsolePlayer { color, name }
    }
}


fn read_from_input() -> io::Result<String> {
    let mut input = String::new();
    io::stdin().read_line(&mut input)?;

    input.pop();
    Ok(input)
}

