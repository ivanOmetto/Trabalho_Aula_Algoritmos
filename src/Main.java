import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static class mapaSensores {
        int id, medida_umidade;
        double ph;
        public Integer getId(){return id;}
    }

    public static void main(String[] args) {
        Scanner le = new Scanner(System.in);
        mapaSensores[] sensores = new mapaSensores[20];
        ArrayList<Integer> ph_alterado = new ArrayList<>();
        cria_sensores(sensores);

        for(int days=0; days < 3; days++) {
            gera_ph_umidade(sensores); // gera novos valores
            valida_ph_alterado(sensores, ph_alterado);
            int sort = consulta_mapa_sensores(le, sensores); // sort mapa sensores
            consulta_sensor(le, sensores, sort); // pesquisa sensor
            for(int id : ph_alterado) System.out.println("Dia: " + (days+1) + " ID sensor alterado: " + id);
        }
    }

    public static void valida_input_int(Scanner le) {
        while(!le.hasNextInt()) {
            System.out.print("Erro, Informe apenas um numero! ");
            le.nextLine();
        }
    }

    public static void cria_sensores(mapaSensores[] sensores) {
        for(int i=0; i < 20; i++) {
            sensores[i] = new mapaSensores();
            sensores[i].id = (int) ((Math.random() * (4799 - 4600)) + 4600);
        }
    }

    public static void gera_ph_umidade(mapaSensores[] sensores) {
        for (int n=0; n < 20; n++) {
            sensores[n].ph = ((Math.random() * (8.f - 4.f)) + 4.f);
            sensores[n].medida_umidade = (int) ((Math.random() * (90 - 20)) + 20);
        }
    }

    public static void valida_ph_alterado(mapaSensores[] sensores, ArrayList<Integer> ph_alterado) {
        for (mapaSensores sensor : sensores) {
            if (!(5.5 <= sensor.ph && 6.5 >= sensor.ph)) {
                if(!ph_alterado.contains(sensor.id)) ph_alterado.add(sensor.id); // adiciona valor a ph_alterado
            }
        }
    }

    public static int consulta_mapa_sensores(Scanner le, mapaSensores[] sensores) {
        System.out.print("Consulta mapa sensores: 1=crescente 2=decrescente ");
        valida_input_int(le);
        int ent = le.nextInt();
        switch (ent) {
            case 1 -> Arrays.sort(sensores, Comparator.comparing(mapaSensores::getId));
            case 2 -> Arrays.sort(sensores, Comparator.comparing(mapaSensores::getId).reversed());
            default -> {
                System.out.println("Erro, Informe um valor valido!");
                consulta_mapa_sensores(le, sensores);
            }
        }
        for(mapaSensores id : sensores) System.out.print(" IDs: " + id.id);
        return ent;
    }

    public static void consulta_sensor(Scanner le, mapaSensores[] sensores, int sort) {
        System.out.print("\nConsulta sensores ou -1 para proceguir: ");
        valida_input_int(le);
        int key = le.nextInt();
        if (key == -1) return;

        if(sort == 1) key = busca_binaria(sensores, key, 0, sensores.length-1);
        else key = busca_binaria_reversa(sensores, key, 0, sensores.length-1);

        if (key == -1) System.out.println("Sensor não disponivel!, tente novamente");
        else System.out.printf("ID: " + sensores[key].id +  " Medida Umidade: " + sensores[key].medida_umidade +
                " PH: %.2f", sensores[key].ph);

        consulta_sensor(le, sensores, sort);
    }

    public static int busca_binaria(mapaSensores[] sensores, int key, int low, int high) {
        int middle = low  + ((high - low) / 2);
        if (high < low) return -1;

        if (key == sensores[middle].id) return middle;
        else if (key < sensores[middle].id) return busca_binaria(sensores, key, low, middle - 1);
        else return busca_binaria(sensores, key, middle + 1, high);
    }

    public static int busca_binaria_reversa(mapaSensores[] sensores, int key, int low, int high) {
        int middle = (high + low) / 2;
        if (high < low) return -1;

        if (key == sensores[middle].id) return middle;
        else if (key > sensores[middle].id) return busca_binaria_reversa(sensores, key, low, middle - 1);
        else return busca_binaria_reversa(sensores, key, middle + 1, high);
    }
}