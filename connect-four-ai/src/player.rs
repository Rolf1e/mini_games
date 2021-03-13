use mg_core::player::Player;

pub struct AiPlayer {
    name: String,
    color: String,
}

impl AiPlayer {
    pub fn new(name: String, color: String) -> Self {
        AiPlayer { name, color }
    }
}

impl Player for AiPlayer {
    fn ask_next_move(&self, board: &mg_core::board::Board) -> mg_core::player::Action {
        todo!()
    }

    fn get_color(&self) -> &str {
        todo!()
    }

    fn get_name(&self) -> &str {
        todo!()
    }
}
