#[cfg(test)]
mod connect_four_test {

    use mg_core::board::Board;
    use mg_core::case::Case; use mg_core::error::Error;
    use mg_core::piece::{Piece, Rank};
    use mg_core::turn::Turn;

    #[test]
    fn should_display_case() {
        let piece = Piece::ConnectFour(String::from("Red"), Rank::ConnectFour(true));
        let case = Case::ConnectFour(piece);

        assert_eq!(String::from("Red1"), case.display());
    }

    #[test]
    fn should_display_board() {
        let empty_row = vec![Case::Empty, Case::Empty];
        let mut board = Board::ConnectFour(vec![empty_row.clone(), empty_row]);
        board
            .place(
                Case::ConnectFour(Piece::ConnectFour(
                    String::from("Red"),
                    Rank::ConnectFour(true),
                )),
                0,
                0,
            )
            .unwrap();
        board.place(Case::Empty, 0, 1).unwrap();

        board
            .place(
                Case::ConnectFour(Piece::ConnectFour(
                    String::from("Yel"),
                    Rank::ConnectFour(false),
                )),
                1,
                0,
            )
            .unwrap();
        board
            .place(
                Case::ConnectFour(Piece::ConnectFour(
                    String::from("Red"),
                    Rank::ConnectFour(true),
                )),
                1,
                1,
            )
            .unwrap();

        assert_eq!(
            String::from("|      | Red1 |\n| Red1 | Yel0 |\n"),
            board.display()
        );
    }

    fn create_board_2x2() -> Result<Board, Error> {
        let empty_row = vec![Case::Empty, Case::Empty];
        Ok(Board::ConnectFour(vec![empty_row.clone(), empty_row]))
    }

    #[test]
    fn should_authorize_play() {
        let mut board = create_board_2x2().unwrap();

        if board.play(Turn::ConnectFour(0)).is_ok() {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_not_authorize_play() {
        let mut board = create_board_2x2().unwrap();

        if let Err(_) = board.play(Turn::ConnectFour(10)) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_at_the_bottom() {
        let mut board = create_board_2x2().unwrap();
        board.play(Turn::ConnectFour(0)).unwrap();
        if let Case::Empty = board.at((0, 0)) {
            assert!(false);
        } else if let Case::ConnectFour(_) = board.at((0, 1)) {
            assert!(false);
        } else {
            assert!(true);
        }
    }

    #[test]
    fn should_not_drop_piece_on_top() {
        let mut board = create_board_2x2().unwrap();
        board.play(Turn::ConnectFour(0)).unwrap();
        board.play(Turn::ConnectFour(0)).unwrap();
        if let Err(_) = board.play(Turn::ConnectFour(0)) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_on_top_of_the_other() {
        let mut board = create_board_3x3().unwrap();
        board.play(Turn::ConnectFour(0)).unwrap();
        board.play(Turn::ConnectFour(0)).unwrap();
        if let Case::Empty = board.at((0, 0)) {
            assert!(false);
        } else if let Case::Empty = board.at((0, 1)) {
            assert!(false);
        } else if let Case::ConnectFour(_) = board.at((0, 2)) {
            assert!(false);
        } else {
            assert!(true);
        }
    }

    fn create_board_3x3() -> Result<Board, Error> {
        let empty_row = vec![Case::Empty, Case::Empty, Case::Empty];
        Ok(Board::ConnectFour(vec![
            empty_row.clone(),
            empty_row.clone(),
            empty_row,
        ]))
    }
}
