import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class AccountDto {

    // 기본 멤버 변수
    private LocalDateTime dateTime;        // yyyy년 mm월 dd일
    private String use;             // Outcome 카테고리(예: 극장구경, 단어수준)
    private String classify;        // Income or Outcome
    private int money;              // 금액
    private String memo;            // 메모(내용)

    public AccountDto(String classify) {
        this.classify = classify;
    }

    public AccountDto(LocalDateTime dateTime, String classify, String use, int money, String memo) {
        this.dateTime = dateTime;
        this.classify = classify;
        this.use = use;
        this.money = money;
        this.memo = memo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Override
    public String toString() {
        return " Added at" + dateTime + classify + " " + use + " " + money + " " + memo;
    }
}

class AccountDao {

    Scanner sc = new Scanner(System.in);

    private List<AccountDto> list = new LinkedList<AccountDto>() {{
        add(0, new AccountDto(LocalDateTime.now(), "Income", "용돈 수령", 500000, "용돈 수령"));
        add(0, new AccountDto(LocalDateTime.of(2021, 12, 18, 10, 15, 20), "Income", "빈병 팔기", 3800, "빈병팔기"));
        add(1, new AccountDto(LocalDateTime.of(2021, 12, 19, 10, 10, 10), "Outcome", "용돈 삭제", 8000, "용돈 삭제"));
        add(2, new AccountDto(LocalDateTime.of(2021, 11, 10, 20, 25, 48), "Income", "폐지줍기", 700, "폐지 많이 주웠다!!"));
        add(2, new AccountDto(LocalDateTime.of(2021, 11, 17, 10, 25, 48), "Outcome", "떡볶이", 3000, "떡볶이"));
        add(3, new AccountDto(LocalDateTime.of(2021, 11, 21, 22, 15, 14), "Outcome", "야쿠르트", 400, "야쿠르트"));
    }};

    // CRUD - Create : 항목생성
    public void create(String classify) {
        // 입력 시간을 저장하기 위한 객체
        LocalDateTime now = LocalDateTime.now();

        System.out.print("키워드를 입력하세요. >>> ");
        // Outcome 카테고리(예: 극장구경, 데이트처럼 단어수준)
        String use = sc.next();

        System.out.print("금액(숫자만)을 입력하세요. >> ");
        int money = sc.nextInt();
        sc.nextLine();

        System.out.print("메모할 내용을 입력하세요. >> ");
        String memo = sc.next();

        list.add(new AccountDto(now, classify, use, money, memo));
    }

    // CRUD - categorization : 항목생성 시 Income, Outcome여부
    public void categorization() {
        System.out.print("Income 또는 Outcome여부를 입력하세요. >> ");
        String classify = sc.next();

        while (true) {
            if (classify.equals("Income")) {
                create(classify);
                break;
            } else if (classify.equals("Outcome")) {
                create(classify);
                break;
            } else {
                System.out.println("잘못된 입력입니다.");
                create(classify);
                break;
            }
        }
    }

    // 모든 데이터 출력
    public void printAllData() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i).toString());
        }
    }

    // 검색(입력한 달의 데이터를 모두 출력, 월간 총Income과 총Outcome 나타내기)
    public void search() {
        // 입력한 연, 월의 데이터 출력
        System.out.print("검색하려는 연도를 입력하세요.(숫자만) >> ");
        int wantSearchYearly = sc.nextInt();
        sc.nextLine();

        System.out.print("검색하려는 월을 입력하세요.(숫자만) >> ");
        int wantSearchMonthly = sc.nextInt();
        sc.nextLine();

        System.out.println("****************************");

        // Income, Outcome 구분해서 넣어주는 리스트
        List<AccountDto> income = new LinkedList<>();
        List<AccountDto> expense = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getClassify().equals("Income")) {
                income.add(list.get(i));
            } else if (list.get(i).getClassify().equals("Outcome")) {
                expense.add(list.get(i));
            }
        }

        // 월간 총Income, Outcome 출력내기
        // Income
        List<AccountDto> sortedIncomeList = new LinkedList<>();
        for (int i = 0; i < income.size(); i++) {
            int checkYear = income.get(i).getDateTime().getYear();
            int checkMonth = income.get(i).getDateTime().getMonthValue();
            if (checkYear == wantSearchYearly && checkMonth == wantSearchMonthly) {
                sortedIncomeList.add(income.get(i));
            }
        }

        for (AccountDto dto : sortedIncomeList) {
            System.out.println(dto);
        }

        System.out.println("****************************");

        // Outcome
        List<AccountDto> sortedExceptionList = new LinkedList<>();
        for (int i = 0; i < expense.size(); i++) {
            int checkYear = expense.get(i).getDateTime().getYear();
            int checkMonth = expense.get(i).getDateTime().getMonthValue();
            if (checkYear == wantSearchYearly && checkMonth == wantSearchMonthly) {
                sortedExceptionList.add(expense.get(i));
            }
        }

        for (AccountDto dto : sortedExceptionList) {
            System.out.println(dto);
        }

        // 총Income
        int total = 0;
        for (int i = 0; i < sortedIncomeList.size(); i++) {
            total += sortedIncomeList.get(i).getMoney();
        }
        System.out.println("총 Income은 " + total + "원 입니다.");

        // 총Outcome
        total = 0;
        for (int i = 0; i < sortedExceptionList.size(); i++) {
            total += sortedExceptionList.get(i).getMoney();
        }
        System.out.println("총 Outcome은 " + total + "원 입니다.");
    }

    // 수정 및 삭제
    public void editData() {
        System.out.print("수정하고자 하는 데이터의 번호를 입력하세요. >> ");
        int wantEditNum = sc.nextInt() - 1;
        sc.nextLine();

        System.out.println("수정할 항목이 없으면 Enter키를 누르세요.");

        AccountDto findAccountDto = list.get(wantEditNum);

        System.out.print("Income 또는 Outcome여부 >> ");
        String inputClassify = sc.nextLine();
        System.out.print("카테고리 입력 >> ");
        String inputUse = sc.nextLine();
        System.out.print("금액 >> ");
        String inputMoney = sc.nextLine();
        //sc.nextLine();
        System.out.print("메모 >> ");
        String inputMemo = sc.nextLine();

        AccountDto accountDto = new AccountDto(findAccountDto.getDateTime(),
                inputClassify.isEmpty() ? findAccountDto.getClassify() : inputClassify,
                inputUse.isEmpty() ? findAccountDto.getUse() : inputUse,
                inputMoney.isEmpty() ? findAccountDto.getMoney() : Integer.parseInt(inputMoney),
                inputMemo.isEmpty() ? findAccountDto.getMemo() : inputMemo);

        list.set(wantEditNum, accountDto);
    }

    public void deleteDate() {
        System.out.print("삭제하고자 하는 데이터의 번호를 입력하세요 >> ");
        int wantDelNum = sc.nextInt() - 1;
        sc.nextLine();

        list.remove(list.get(wantDelNum));
    }

    // 내용(AccountDto 클래스의 멤버변수 memo) 검색어로 산출된 데이터 출력
    public void searchWithMemoVariable() {
        System.out.print("검색할 메모 내용을 입력하세요 >> ");
        String searchMemo = sc.next();

        for (AccountDto accountDto : list) {
            if (accountDto.getMemo().contains(searchMemo)) {
                System.out.println(accountDto);
            }
        }
    }

    // txt파일로 저장하는 기능을 메뉴에 추가하고 리스트로 다시 불러오는 기능 구현
    public void saveAsTextFile() {
        File newFile = new File("C:\\Users\\livin\\JavaProg\\Account.text");

        try {
            newFile.createNewFile();
        } catch (Exception e) {
            System.out.println("파일 생성에 실패하였습니다.");
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile));
            PrintWriter pw = new PrintWriter(bw);

            StringBuilder result = new StringBuilder();

            for (AccountDto accountDto : list) {
                result.append(accountDto.getDateTime()).append(", ");
                result.append(accountDto.getClassify()).append(", ");
                result.append(accountDto.getMoney()).append(", ");
                result.append(accountDto.getMemo()).append(", ");
                result.append(accountDto.getUse()).append(" ");
                result.append("\n");
            }
            pw.print(result);

            pw.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter(newFile, true);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                for (AccountDto accountDto : list) {
                    result.append(accountDto.toString());
                }
            }
            fw.write(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFileAsList() throws IOException {
        File origin = new File("C:\\Users\\livin\\JavaProg\\Account.text");

        BufferedReader br = new BufferedReader(new FileReader(origin));

        String line;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
            String[] s = line.split(", ");

            AccountDto accountDto = new AccountDto(
                    LocalDateTime.parse(s[0]), s[1], s[3], Integer.parseInt(s[2]), s[4]
            );

            list.add(accountDto);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        // composition of menus

        Scanner sc = new Scanner(System.in);

        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
        System.out.println(dateTime);

        AccountDao dao = new AccountDao();

        while (true) {
            // CRUD
            System.out.println("*****************************");
            System.out.println("*********가계부 ver1.0*********");
            System.out.println("**** 1. Income/Outcome 항목 입력 *****");
            System.out.println("**** 2. 전체 데이터 열람 ********");
            System.out.println("**** 3. 연/월별 데이터 검색 *****");
            System.out.println("**** 4. 데이터 수정 ***********");
            System.out.println("**** 5. 데이터 삭제 ***********");
            System.out.println("**** 6. 메모 내용으로 검색 *****");
            System.out.println("**** 7. 내역을 파일로 저장 *****");
            System.out.println("**** 8. 파일을 콘솔에 출력 *****");
            System.out.println("**** 9. 프로그램 종료 *********");
            System.out.println("*****************************");
            System.out.print("사용할 메뉴 번호 입력 >> ");
            int select = sc.nextInt();
            sc.nextLine();

            switch (select) {
                case 1:
                    dao.categorization();
                    break;
                case 2:
                    dao.printAllData();
                    break;
                case 3:
                    dao.search();
                    break;
                case 4:
                    dao.editData();
                    break;
                case 5:
                    dao.deleteDate();
                    break;
                case 6:
                    dao.searchWithMemoVariable();
                    break;
                case 7:
                    dao.saveAsTextFile();
                    break;
                case 8:
                    try {
                        dao.readFileAsList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    System.exit(0);
                    break;
                default:
                    System.out.println("없는 메뉴입니다. 다시 시도하세요.");
                    break;
            }
        }
    }
}
