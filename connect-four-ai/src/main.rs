use mg_core::board::Board;
use mg_core::case::Case;
use mg_core::error::MGError;
use mg_core::games::connect_four::ConnectFourColor;
use mg_core::player::Action;

fn main() {
    let mut board = create_board_3x3().unwrap();

    board
        .play(Action::ConnectFour(0, ConnectFourColor::Yellow))
        .unwrap();
    board
        .play(Action::ConnectFour(0, ConnectFourColor::Red))
        .unwrap();
    board
        .play(Action::ConnectFour(1, ConnectFourColor::Yellow))
        .unwrap();
    board
        .play(Action::ConnectFour(0, ConnectFourColor::Red))
        .unwrap();
    println!("{}", board.display());
}

fn create_board_3x3() -> Result<Board, MGError> {
    let empty_row = vec![Case::Empty, Case::Empty, Case::Empty];
    Ok(Board::ConnectFour(vec![
        empty_row.clone(),
        empty_row.clone(),
        empty_row,
    ]))
}
