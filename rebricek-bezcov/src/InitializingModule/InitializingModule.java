package InitializingModule;

import InteractionModule.InteractionModule;
import java.io.IOException;

public class InitializingModule {
    private static String help =
            "Possible initialization scenarios:\n" +
                    "\'JARFILE -a\' for adding a new input file and running the point computation\n" +
                    "\'JARFILE -a fileAddress\' for adding a new input file (with given fileAddress) and running the point computation\n" +
                    "\'JARFILE -b\' for running the point computation, with actual input files only.";
    // toto by mohla byt ta trieda, ktora si bude drzat referencie na moduly a bude mat staticke gettery, ktore si mozu volat hocijake ine moduly
    public void run(char initSwitch, String inputAddress) throws IOException {    // initSwitch = 'a' / 'b' / 'c' (ani jedno)
        switch (initSwitch){
            case 'a':
                new InteractionModule().runA(inputAddress);
                break;
            case 'b':
                new InteractionModule().runB();
                break;
            case 'c':
                new InteractionModule().run();
                break;
            default:
                System.out.println("Wrong switch");
                new InteractionModule().run();
                break;
        }
    }
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            new InitializingModule().run('c',null);
            return;
        } else {
            String arg = args[0];
            if ("-".equals(arg.substring(0, 1))) {
                arg = arg.substring(1);
            }
            arg = arg.trim();
            switch (arg) {
                case "a":
                    if (args.length == 1) {
                        // initializing module to posle na interaction module, ten to vyhodnoti ako zlu adresu a vypyta si novu
                        new InitializingModule().run('a',null);
                    }
                    else if (args.length == 2){
                        new InitializingModule().run('a',args[1]);
                    }
                    return;
                case "b":
                    if (args.length > 1) {
                        System.out.println("Variant b selected, any other arguments are ignored");
                    }
                    new InitializingModule().run('b',null);
                    return;
                case "help":
                    System.out.println(help);
                    break;
                default:
                    System.out.println("Selected variant is invalid");
                    System.out.println("For help, run the program with \'-help\' argument");
                    new InitializingModule().run('c',null);
                    return;
            }
        }
    }
}