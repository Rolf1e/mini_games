#[cfg(test)]
mod connect_four_test {

    use mg_core::board::Board;
    use mg_core::case::Case;
    use mg_core::error::Error;
    use mg_core::games::connect_four::{ConnectFourColor, ConnectFourState};
    use mg_core::operator::Operator;
    use mg_core::piece::{Piece, Rank};
    use mg_core::player::Action;
    use mg_core::player::Player;
    use mg_core::state::State;

    const EMPTY: &str = "     ";
    const RED: &str = " Red ";
    const YELLOW: &str = " Yel ";

    const RED_CASE: Case =
        Case::ConnectFour(Piece::ConnectFour(ConnectFourColor::Red, Rank::ConnectFour));
    const YELLOW_CASE: Case = Case::ConnectFour(Piece::ConnectFour(
        ConnectFourColor::Yellow,
        Rank::ConnectFour,
    ));

    struct TestPlayer {
        name: String,
        color: ConnectFourColor,
    }

    struct TestPlayer2 {
        color: ConnectFourColor,
    }

    impl Player for TestPlayer2 {
        fn ask_next_move(&self, _board: &Board) -> Action {
            match &self.color {
                ConnectFourColor::Red => Action::ConnectFour(0, ConnectFourColor::Red),
                ConnectFourColor::Yellow => Action::ConnectFour(1, ConnectFourColor::Yellow),
                ConnectFourColor::Equality => panic!(),
            }
        }

        fn get_color(&self) -> &str {
            todo!("Don't think this is really useful for test case")
        }

        fn get_name(&self) -> &str {
            todo!("Don't think this is really useful for test case")
        }
    }

    impl Player for TestPlayer {
        fn ask_next_move(&self, _board: &Board) -> Action {
            match &self.color {
                ConnectFourColor::Red => Action::ConnectFour(0, ConnectFourColor::Red),
                ConnectFourColor::Yellow => Action::ConnectFour(0, ConnectFourColor::Yellow),
                ConnectFourColor::Equality => panic!(),
            }
        }

        fn get_color(&self) -> &str {
            todo!("Don't think this is really useful for test case")
        }

        fn get_name(&self) -> &str {
            self.name.as_str()
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

    fn create_2x2_game() -> Operator {
        let board = create_board_2x2().unwrap();
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };
        Operator::new(board, Box::new(player), Box::new(player_2))
    }

    fn create_3x3_game() -> Operator {
        let board = create_board_3x3().unwrap();
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };
        Operator::new(board, Box::new(player), Box::new(player_2))
    }

    fn create_board_2x2() -> Result<Board, Error> {
        let empty_row = vec![Case::Empty, Case::Empty];
        Ok(Board::ConnectFour(vec![empty_row.clone(), empty_row]))
    }

    #[test]
    fn should_display_case() {
        let piece = Piece::ConnectFour(ConnectFourColor::Red, Rank::ConnectFour);
        let case = Case::ConnectFour(piece);

        assert_eq!(String::from("Red"), case.display());
    }

    #[test]
    fn should_display_board() {
        let empty_row = vec![Case::Empty, Case::Empty, Case::Empty];
        let mut board = Board::ConnectFour(vec![empty_row.clone(), empty_row.clone(), empty_row]);

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

        let expected = format!(
            "|{}|{}|{}|\n|{}|{}|{}|\n|{}|{}|{}|\n",
            RED, EMPTY, EMPTY, RED, EMPTY, EMPTY, YELLOW, YELLOW, EMPTY
        );
        assert_eq!(expected, board.display());
    }

    #[test]
    fn should_authorize_play() {
        let mut board = create_board_2x2().unwrap();

        if board
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
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

        if let Err(_) = board.play(Action::ConnectFour(10, ConnectFourColor::Red)) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_at_the_bottom() {
        let mut board = create_board_2x2().unwrap();
        board
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
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
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
            .unwrap();
        eprintln!("{}", board.display());
        board
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
            .unwrap();
        eprintln!("{}", board.display());
        if let Err(_) = board.play(Action::ConnectFour(0, ConnectFourColor::Yellow)) {
            assert!(true);
        } else {
            assert!(false);
        }
    }

    #[test]
    fn should_drop_piece_on_top_of_the_other() {
        let mut board = create_board_3x3().unwrap();
        board
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
            .unwrap();
        board
            .play(Action::ConnectFour(0, ConnectFourColor::Red))
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

    #[test]
    fn should_play_one_turn() {
        let mut operator = create_3x3_game();

        if let Err(_) = operator.play() {
            assert!(false);
        }
    }

    #[test]
    fn should_not_play() {
        let mut operator = create_3x3_game();

        if let Err(e) = operator.play() {
            assert_eq!(Error::IllegalMove(String::from("This move is illegal")), e);
        }
    }

    // #[test]
    fn should_let_play() {
        let mut operator = create_3x3_game();
        if let Err(_) = operator.play() {
            assert!(false);
        }

        let state = operator.state();
        let expected: &dyn State = &ConnectFourState::Yellow;
        assert_eq!(expected.message(), state.message());
    }

    // #[test]
    fn should_stop_playing() {
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };

        let board = Board::ConnectFour(vec![
            vec![RED_CASE, YELLOW_CASE],
            vec![RED_CASE, YELLOW_CASE],
        ]);

        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));
        let state = operator.state();

        let expected: &dyn State = &ConnectFourState::Over(None);
        assert_eq!(expected.message(), state.message());
    }

    #[test]
    fn should_have_winner_row() {
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };
        let board = Board::ConnectFour(vec![
            vec![YELLOW_CASE, RED_CASE, YELLOW_CASE, YELLOW_CASE],
            vec![YELLOW_CASE, RED_CASE, Case::Empty, Case::Empty],
            vec![RED_CASE, RED_CASE, Case::Empty, Case::Empty],
            vec![YELLOW_CASE, RED_CASE, Case::Empty, Case::Empty],
        ]);

        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));
        let state = operator.state();

        let expected: &dyn State = &ConnectFourState::Over(Some(ConnectFourColor::Red));
        assert_eq!(expected.message(), state.message());
    }

    #[test]
    fn should_have_winner_column() {
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };

        let board = Board::ConnectFour(vec![
            vec![RED_CASE, YELLOW_CASE, RED_CASE, RED_CASE],
            vec![YELLOW_CASE, Case::Empty, Case::Empty, Case::Empty],
            vec![YELLOW_CASE, YELLOW_CASE, YELLOW_CASE, YELLOW_CASE],
            vec![YELLOW_CASE, Case::Empty, Case::Empty, Case::Empty],
        ]);

        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));
        let state = operator.state();

        let expected: &dyn State = &ConnectFourState::Over(Some(ConnectFourColor::Yellow));
        assert_eq!(expected.message(), state.message());
    }

    #[test]
    fn should_have_winner_diagonal() {
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };
        let board = Board::ConnectFour(vec![
            vec![YELLOW_CASE, Case::Empty, Case::Empty, Case::Empty],
            vec![RED_CASE, YELLOW_CASE, Case::Empty, Case::Empty],
            vec![YELLOW_CASE, RED_CASE, YELLOW_CASE, Case::Empty],
            vec![RED_CASE, RED_CASE, RED_CASE, YELLOW_CASE],
        ]);

        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));
        let state = operator.state();

        let expected: &dyn State = &ConnectFourState::Over(Some(ConnectFourColor::Yellow));
        assert_eq!(expected.message(), state.message());
    }

    // #[test]
    fn should_have_no_winner_because_board_is_full() {
        let player = TestPlayer {
            color: ConnectFourColor::Yellow,
            name: String::from("Tigran"),
        };
        let player_2 = TestPlayer {
            color: ConnectFourColor::Red,
            name: String::from("Cassiopée"),
        };
        let board = Board::ConnectFour(vec![
            vec![YELLOW_CASE, YELLOW_CASE, RED_CASE, YELLOW_CASE],
            vec![RED_CASE, YELLOW_CASE, YELLOW_CASE, RED_CASE],
            vec![YELLOW_CASE, RED_CASE, YELLOW_CASE, RED_CASE],
            vec![RED_CASE, RED_CASE, RED_CASE, YELLOW_CASE],
        ]);

        let mut operator = Operator::new(board, Box::new(player), Box::new(player_2));
        let state = operator.state();

        let expected: &dyn State = &ConnectFourState::Over(Some(ConnectFourColor::Equality));
        assert_eq!(expected.message(), state.message());
    }
}
