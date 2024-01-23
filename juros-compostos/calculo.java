import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class CalculoJurosCompostosAvancado {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar entrada do usuário
        System.out.print("Digite o valor principal: ");
        double principal = scanner.nextDouble();

        System.out.print("Digite a taxa de juros anual (em porcentagem): ");
        double taxaJurosAnual = scanner.nextDouble();

        System.out.print("Digite o número de anos: ");
        int anos = scanner.nextInt();

        System.out.print("Digite a data inicial (formato dd/MM/yyyy): ");
        String dataInicialStr = scanner.next();

        System.out.print("Escolha a frequência de capitalização (D - Diária, M - Mensal, A - Anual): ");
        char escolhaCapitalizacao = scanner.next().charAt(0);

        // Converter a string da data para o objeto Date
        Date dataInicial = converterStringParaData(dataInicialStr);

        // Calcular juros compostos
        double montanteFinal = calcularJurosCompostos(principal, taxaJurosAnual, anos, dataInicial, escolhaCapitalizacao);

        // Exibir resultado
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        System.out.println("O montante final após " + anos + " anos é: " + currencyFormatter.format(montanteFinal));

        scanner.close();
    }

    private static double calcularJurosCompostos(double principal, double taxaJurosAnual, int anos, Date dataInicial, char escolhaCapitalizacao) {
        int totalCapitalizacoesPorAno = obterTotalCapitalizacoesPorAno(escolhaCapitalizacao);
        double taxaJurosMensal = taxaJurosAnual / (totalCapitalizacoesPorAno * 100);
        int totalPeriodos = anos * totalCapitalizacoesPorAno;

        // Calcular juros compostos considerando a data inicial
        long diasDecorridos = calcularDiasDecorridos(dataInicial, new Date());
        double fatorTemporal = Math.pow(1 + taxaJurosMensal / totalCapitalizacoesPorAno, totalPeriodos * (diasDecorridos / 365.0));

        return principal * fatorTemporal;
    }

    private static int obterTotalCapitalizacoesPorAno(char escolhaCapitalizacao) {
        switch (Character.toUpperCase(escolhaCapitalizacao)) {
            case 'D':
                return 365;
            case 'M':
                return 12;
            case 'A':
                return 1;
            default:
                throw new IllegalArgumentException("Escolha de capitalização inválida");
        }
    }

    private static long calcularDiasDecorridos(Date dataInicial, Date dataFinal) {
        long diferenca = dataFinal.getTime() - dataInicial.getTime();
        return diferenca / (24 * 60 * 60 * 1000);
    }

    private static Date converterStringParaData(String dataStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(dataStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use o formato dd/MM/yyyy.");
        }
    }
}
