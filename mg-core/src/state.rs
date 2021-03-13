pub trait State: PartialEq + Eq + Sized {
    //fn next(&self) -> &dyn State;
    
    fn message(&self) -> String;
}
