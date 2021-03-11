#[cfg(test)]
mod connect_four_test {

    use mg_core::board::Board;
    use mg_core::case::Case;
    use mg_core::error::Error;
    use mg_core::operator::Operator;
    use mg_core::piece::{Piece, Rank};
    use mg_core::player::Action;
    use mg_core::player::Player;

    struct TestPlayer {
        name: String,
        color: String,
    }

    impl Player for TestPlayer {
        fn ask_next_move(&self, _board: &Board) -> Action {
            Action::ConnectFour(0, String::from("Red"))
        }

        fn get_color(&self) -> &str {
            self.color.as_str()
        }

        fn get_name(&self) -> &str {
            self.name.as_str()
        }
    }

    #[test]
    fn should_display_case() {
        let piece = Piece::ConnectFour(String::from("Red"), Rank::ConnectFour);
        let case = Case::ConnectFour(piece);

        assert_eq!(String::from("Red"), case.display());
    }

    const EMPTY: &str = "     ";
    const RED: &str = " Red ";
    const YELLOW: &str = " Yel ";

    #[test]
    fn should_display_board() {
        let empty_row = vec![Case::Empty, Case::Empty, Case::Empty];
        let mut board = Board::ConnectFour(vec![empty_row.clone(), empty_row.clone(), empty_row]);

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

        let expected = format!(
            "|{}|{}|{}|\n|{}|{}|{}|\n|{}|{}|{}|\n",
            RED, EMPTY, EMPTY, RED, EMPTY, EMPTY, YELLOW, YELLOW, EMPTY
        );
        assert_eq!(expected, board.display());
    }

    fn create_board_2x2() -> Result<Board, Error> {
        let empty_row = vec![Case::Empty, Case::Empty];
        Ok(Board::ConnectFour(vec![empty_row.clone(), empty_row]))
    }

    #[test]
    fn should_authorize_play() {
        let mut board = create_board_2x2().unwrap();

        if board
            .play(Action::ConnectFour(0, String::from("Red")))
            .is_ok()
        {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_not_authorize_play() {
        let mut board = create_board_2x2().unwrap();

        if let Err(_) = board.play(Action::ConnectFour(10, String::from("Red"))) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_at_the_bottom() {
        let mut board = create_board_2x2().unwrap();
        board
            .play(Action::ConnectFour(0, String::from("Red")))
            .unwrap();
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
        board
            .play(Action::ConnectFour(0, String::from("Red")))
            .unwrap();
        eprintln!("{}", board.display());
        board
            .play(Action::ConnectFour(0, String::from("Red")))
            .unwrap();
        eprintln!("{}", board.display());
        if let Err(_) = board.play(Action::ConnectFour(0, String::from("Yel"))) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_on_top_of_the_other() {
        let mut board = create_board_3x3().unwrap();
        board
            .play(Action::ConnectFour(0, String::from("Red")))
            .unwrap();
        board
            .play(Action::ConnectFour(0, String::from("Red")))
            .unwrap();

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

    #[test]
    fn should_play_one_turn() {
        let board = create_board_3x3().unwrap();
        let player = TestPlayer {
            color: String::from("Red"),
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: String::from("Yel"),
            name: String::from("Cassiopée"),
        };
        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));

        if let Err(_) = operator.play() {
            assert!(false);
        }
    }

    #[test]
    fn should_not_play() {
        let board = create_board_3x3().unwrap();
        let player = TestPlayer {
            color: String::from("Yel"),
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: String::from("Red"),
            name: String::from("Cassiopée"),
        };
        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));

        if let Err(e) = operator.play() {
            assert_eq!(Error::IllegalMove(String::from("This move is illegal")), e);
        }
    }
}
