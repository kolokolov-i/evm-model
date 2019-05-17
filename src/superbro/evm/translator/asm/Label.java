package superbro.evm.translator.asm;

class Label {
    Word token;
    Instruct target;

    Label(Word token, Instruct target){
        this.token = token;
        this.target = target;
    }
}
