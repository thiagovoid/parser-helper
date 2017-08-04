/**
 * Parser de convers&atilde;o de arquivos XLSX para JSON
 * @author Thiago Franco
 * @since 04/01/2017
 * @version 1.0
 */
public class XLSXParser {

	@SuppressWarnings("deprecation")
	public static String toJSON(FileUpload upload) {
		
		// Define nó principal do json
		JSONObject rootNode = new JSONObject();
		
		try {
			// Lista que guarda os títulos das colunas da planilha
			List<String> headers = new ArrayList<>();
			
			// Controle de primeira linha (headers)
			boolean firstLine = true;
			
			// Instancia o workbook do arquivo XLS 
			XSSFWorkbook workbook = new XSSFWorkbook(upload.getInpuStream());

			// Percorre as planilhas do arquivo XLS
			for (Iterator<Sheet> iterator = workbook.iterator(); iterator.hasNext();) {
				XSSFSheet sheet = (XSSFSheet) iterator.next();
				
				// Define nó de cada linha de cada planilha
				JSONArray rowNode = new JSONArray();
				
				// Define nó de cada célula de cada planilha
				JSONObject cellNode = new JSONObject();
				
				// Percorre cada linha de cada planilha
				Iterator<Row> rowIterator = sheet.iterator();
				while(rowIterator.hasNext()) {
					Row row = rowIterator.next();

					// Define índice de cada coluna
					int index = 0;

					// Para cada linha, percorre cada coluna
					Iterator<Cell> cellIterator = row.cellIterator();
					while(cellIterator.hasNext()) {
						
						Cell cell = cellIterator.next();

						// Se for primeira linha, guarda nome da coluna
						if (firstLine) {
							headers.add(cell.getStringCellValue());
						} else {
						
							// Verifica tipo de célula e adiciona no nó de célula
							switch(cell.getCellTypeEnum()) {
								case BOOLEAN:
									cellNode.put(headers.get(index), cell.getBooleanCellValue());
									break;
								case NUMERIC:
									cellNode.put(headers.get(index), cell.getNumericCellValue());
									break;
								case STRING:
									cellNode.put(headers.get(index), cell.getStringCellValue());
									break;
								default:
									cellNode.put(headers.get(index), "");
									break;
							}
							
							// Incrementa o índice da coluna
							index++;
						}
						
					}
					// Ignora a primeira linha no nó de linhas, pois a primeira é o cabeçalho
					if (!firstLine)
						rowNode.add(cellNode);
					
					// Após verificar primeira linham marca variável de primeira linha para false
					firstLine = false;
					
				}
				
				// Adiciona todas as linhas no nó principal
				rootNode.put(sheet.getSheetName(), rowNode);
				
				// Reseta variáveis para próxima planilha
				firstLine = true;
				headers.clear();
			}
			
			// Fecha arquivo
			workbook.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return rootNode.toString();
	}
}
