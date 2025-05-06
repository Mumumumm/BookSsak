import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Timer {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private long startTimeMillis; // 시작 시간을 밀리초로 저장

    // Timer
    public ResultReadBook bookTimer() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Scanner scanner = new Scanner(System.in);
        System.out.println("⏰독서 시작! 중지하려면 Enter 키를 누르세요");

        // 시작 시간 기록 (밀리초 단위)
        startTimeMillis = System.currentTimeMillis();
        running.set(true);

        // 주기적으로 화면 업데이트하는 작업 정의
        Runnable displayTask = () -> {
            if (running.get()) {
                long nowMillis = System.currentTimeMillis();
                long elapsedMillis = nowMillis - startTimeMillis; // 경과 시간 (밀리초)
                long totalSeconds = elapsedMillis / 1000;       // 총 경과 시간 (초)

                // 시간, 분, 초 계산
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                // HH:MM:SS 형식으로 포맷 (항상 두 자리로 표시)
                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                // \r을 사용하여 현재 줄에 덮어쓰기
                System.out.print("\r경과 시간: " + formattedTime + "  "); // 뒤에 공백 추가
            }
        };

        // 0초 딜레이 후 시작하여 1초 간격으로 displayTask 실행
        executor.scheduleAtFixedRate(displayTask, 0, 1, TimeUnit.SECONDS);

        // 메인 스레드는 여기서 사용자 입력을 기다림 (Enter 키)
        scanner.nextLine();

        // --- Enter 키가 입력된 후 ---

        // 1. 실행 상태 플래그를 false로 변경
        running.set(false);

        // 2. 최종 경과 시간 계산 및 저장 (초 단위)
        long endTimeMillis = System.currentTimeMillis();
        long finalElapsedMillis = endTimeMillis - startTimeMillis;
        int finalElapsedSeconds = (int) finalElapsedMillis / 1000; // 최종 경과 시간을 초 단위로 저장

        // 3. ScheduledExecutorService 종료
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) { // 1초간 종료 대기
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // 4. 최종 결과 출력
        // 마지막 업데이트 내용 지우고 최종 결과 출력
        System.out.println();
        System.out.print("\r" + " ".repeat(50) + "\r"); // 캐리지 리턴으로 줄 시작으로 가서 공백으로 덮어씀
        System.out.println("⏰독서 종료!");

        // 저장된 최종 시간을 HH:MM:SS 형식으로 다시 포맷하여 보여주기
        long finalHours = finalElapsedSeconds / 3600;
        long finalMinutes = (finalElapsedSeconds % 3600) / 60;
        long finalSecondsPart = finalElapsedSeconds % 60;
        String finalFormattedTime = String.format("%02d:%02d:%02d", finalHours, finalMinutes, finalSecondsPart);


//        System.out.println("독서 시간: " + finalFormattedTime);


        // 저장된 finalElapsedSeconds 변수를 이제 다른 로직에서 사용할 수 있습니다.
        // 예: if (finalElapsedSeconds > 60) { ... }

        Scanner input = new Scanner(System.in);

        System.out.println("\uD83D\uDCDD이번 챌린지는 몇 쪽까지 읽으셨나요?");
        int pages = input.nextInt();
        input.nextLine();
        System.out.println("*****************************");
        System.out.println("챌린지가 종료되었습니다 짝짝짝 \uD83D\uDC4F");
        System.out.println("이번 읽은 페이지 " + pages);
        System.out.println("이번 독서 시간 " + finalFormattedTime);
        System.out.println("*****************************");

        return new ResultReadBook(finalFormattedTime, pages);
    }
}
