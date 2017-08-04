/**
 * Parser de convers&atilde;o de arquivos CSV para JSON
 * @author Thiago Franco
 * @since 03/01/2017
 * @version 1.0
 */
public class CSVParser {

	public static String toJSON(FileUpload upload) {
		
		// Definição de variáveis locais
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        
        // Define nó principal do json
        JSONArray rootNode = new JSONArray();
        
        // Lista que guarda os títulos das colunas da planilha
        String[] headers = null;
        String[] values = null;

        try {

        	// Define o input stream para leitura do arquivo
        	InputStreamReader input = new InputStreamReader(upload.getInpuStream());
            br = new BufferedReader(input);
            
            // Controle de primeira linha (headers)
            boolean firstLine = true;
            
            // Percorre cada linha do arquivo csv
            while ((line = br.readLine()) != null) {

            	// Se for primeira linha, guarda nome da coluna
            	if (firstLine) {
            		headers = line.split(cvsSplitBy);
            		firstLine = false;
            	} else {
            		// Define nó de cada célula do arquivo
            		JSONObject cellNode = new JSONObject();
	                values = line.split(cvsSplitBy);
	
	                // Adiciona valor no json
	                for (int i = 0; i < values.length; i++) {
	                	cellNode.put(headers[i], values[i]);
					}
	                
	                // Adiciona valores no nó principal
	                rootNode.add(cellNode);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
        	// Fecha arquivo
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		return rootNode.toString();
	}
}
