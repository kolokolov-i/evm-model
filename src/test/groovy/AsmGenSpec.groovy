import spock.lang.Specification
import spock.lang.Unroll
import superbro.evm.translator.Messager
import superbro.evm.translator.asm.Instruct
import superbro.evm.translator.asm.Lexer
import superbro.evm.translator.asm.Syntaxer
import superbro.evm.translator.asm.Token

class AsmGenSpec extends Specification {

    Messager messager = new Messager()
    Lexer lexer = new Lexer()
    ArrayList<Short> raw = new ArrayList<>(2)

    @Unroll
    def "generate #command"(){
        given:
        List<Token> toks = new ArrayList<>(10)
        messager.reset()
        raw.clear()
        boolean  t = lexer.scan(command, 0, toks, messager)
        assert t
        Syntaxer syntaxer = new Syntaxer(toks, messager)
        t = syntaxer.parse()
        assert t
        List<Instruct> instructs = syntaxer.getResult()
        Instruct.generate(instructs, raw, messager)

        expect:
        raw.get(0) == code

        where:
        command     | code
        // control commands
        "nop"       | 0x0000
        "call RM0"  | 0x0140
        "call RM1"  | 0x0141
        "rcall 0xA5"| 0x02A5
        "ret"       | 0x0150
        "retn 0xA5" | 0x03A5
        "int 5"     | 0x0165
        "iret"      | 0x0170
        "jump RM0"  | 0x0180
        "jump RM2"  | 0x0182
        "rjmp 0xA5" | 0x04A5
        "rjmp 0x15" | 0x0415
        "jmpZ 0xA5" | 0x08A5
        "jmpZ 0x15" | 0x0815
        "jmpS 0xA5" | 0x09A5
        "jmpS 0x15" | 0x0915
        "jmpC 0xA5" | 0x0AA5
        "jmpC 0x15" | 0x0A15
        "jmpO 0xA5" | 0x0BA5
        "jmpO 0x15" | 0x0B15
        "jmpNZ 0xA5"| 0x0CA5
        "jmpNZ 0x15"| 0x0C15
        "jmpNS 0xA5"| 0x0DA5
        "jmpNS 0x15"| 0x0D15
        "jmpNC 0xA5"| 0x0EA5
        "jmpNC 0x15"| 0x0E15
        "jmpNO 0xA5"| 0x0FA5
        "jmpNO 0x15"| 0x0F15
        // flags commands

        // logic commands

        // shift commands

        // arithmetic commands

        // data commands

    }


}
