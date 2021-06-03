use mg_core::board::Board;
use mg_core::case::Case;
use mg_core::error::MGError;
use mg_core::games::connect_four::{ConnectFourColor, ConnectFourState};
use mg_core::operator::Operator;
use mg_core::player::{Color, ConsolePlayer};

fn main() {
    match operator() {
        Ok(mut operator) => loop { plays_one_turn(&mut operator)}
        Err(e) => panic!("Failed to create ConnectFour operator, {}", e.message()),
    }

}

fn plays_one_turn(operator: &mut Operator) {
    if let Err(e) = operator.play() {
        panic!("{}", e.message())
    } else {
        println!("{}", operator.display());
    }
}

fn operator() -> Result<Operator, MGError> {
    Ok(Operator::new(
        create_board()?,
        Box::new(ConsolePlayer::new(
            Color::ConnectFour(ConnectFourColor::Red),
            String::from("Malokran"),
        )),
        Box::new(ConsolePlayer::new(
            Color::ConnectFour(ConnectFourColor::Yellow),
            String::from("Tigran"),
        )),
        Box::new(ConnectFourState::Red),
    ))
}

fn create_board() -> Result<Board, MGError> {
    let empty_row = vec![
        Case::Empty,
        Case::Empty,
        Case::Empty,
        Case::Empty,
        Case::Empty,
        Case::Empty,
    ];
    Ok(Board::ConnectFour(vec![
        empty_row.clone(),
        empty_row.clone(),
        empty_row.clone(),
        empty_row.clone(),
        empty_row.clone(),
        empty_row.clone(),
        empty_row,
    ]))
}
