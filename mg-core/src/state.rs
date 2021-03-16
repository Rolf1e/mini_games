pub trait State {
    fn next(&mut self);
    
    fn message(&self) -> String;
}

