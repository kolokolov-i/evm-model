package superbro.evm.translator.asm;

public enum CharCategory {
    p, a, d, m, n, s, u, ba, bb, other;

    static CharCategory get(char c){
        switch(c){
            case ' ':
            case '\t':
                return CharCategory.p;
            case '\n':
            case ';':
                return CharCategory.n;
            case ':':
                return CharCategory.s;
            case ',':
                return CharCategory.m;
            case '+':
                return CharCategory.u;
            case '[':
                return CharCategory.ba;
            case ']':
                return CharCategory.bb;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return CharCategory.d;
            default:
                if(Character.isAlphabetic(c)){
                    return CharCategory.a;
                }
                else{
                    return CharCategory.other;
                }
        }
    }
}
