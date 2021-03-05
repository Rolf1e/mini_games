use mg_core::board::Board;
use mg_core::case::Case;
use mg_core::error::Error;
use mg_core::turn::Action;

fn main() {
    let mut board = create_board_3x3().unwrap();

    board
        .play(Action::ConnectFour(0, String::from("Yel")))
        .unwrap();
    board
        .play(Action::ConnectFour(0, String::from("Red")))
        .unwrap();
    board
        .play(Action::ConnectFour(1, String::from("Yel")))
        .unwrap();
    board
        .play(Action::ConnectFour(0, String::from("Red")))
        .unwrap();
    println!("{}", board.display());
}

fn create_board_3x3() -> Result<Board, Error> {
    let empty_row = vec![Case::Empty, Case::Empty, Case::Empty];
    Ok(Board::ConnectFour(vec![
        empty_row.clone(),
        empty_row.clone(),
        empty_row,
    ]))
}
