package kuke.board.common.snowflake;

import java.util.random.RandomGenerator;

public class Snowflake {
	// 사용되지 않는 비트 (일반적으로 1비트 사용) 
	private static final int UNUSED_BITS = 1;
	// 타임스탬프를 저장할 비트 수 (41비트)
	private static final int EPOCH_BITS = 41;
	// 노드 ID(서버 또는 인스턴스 ID)를 저장할 비트 수 (10비트)
	private static final int NODE_ID_BITS = 10;
	// 시퀀스 번호(같은 밀리초 내에서 생성 순서를 나타냄)를 저장할 비트 수 (12비트)
	private static final int SEQUENCE_BITS = 12;

	// 노드 ID의 최대값 (10비트이므로 2^10 - 1 = 1023)
	private static final long maxNodeId = (1L << NODE_ID_BITS) - 1;
	
	private static final long maxSequence = (1L << SEQUENCE_BITS) - 1;
	// 시퀀스 번호의 최대값 (12비트이므로 2^12 - 1 = 4095)

	// 노드 ID를 무작위로 생성 (0부터 maxNodeId까지의 값 중 하나)
	private final long nodeId = RandomGenerator.getDefault().nextLong(maxNodeId + 1);
	
	
	// UTC = 2024-01-01T00:00:00Z
	// Snowflake 알고리즘의 기준 시간이 되는 시작 시간 (2024년 1월 1일 00:00:00 UTC)
	private final long startTimeMillis = 1704067200000L;

	// 마지막으로 ID를 생성한 시간 (초기값은 시작 시간)
	private long lastTimeMillis = startTimeMillis;

	// 시퀀스 번호 (동일한 밀리초 내에서 증가하는 값)
	private long sequence = 0L;

	// 새로운 ID를 생성하는 메서드 (멀티스레드 환경에서도 안전하도록 synchronized 사용)
	public synchronized long nextId() {
		// 현재 시스템 시간을 밀리초 단위로 가져옴
		long currentTimeMillis = System.currentTimeMillis();

		// 현재 시간이 lastTimeMillis보다 과거라면, 시계가 거꾸로 갔다고 판단하여 예외 발생
		if (currentTimeMillis < lastTimeMillis) {
			throw new IllegalStateException("Invalid Time");
		}

		// 같은 밀리초 내에서 요청이 들어온 경우
		if (currentTimeMillis == lastTimeMillis) {
			// 시퀀스 번호 증가 (4095까지 가능)
			sequence = (sequence + 1) & maxSequence;
			if (sequence == 0) {

				// 시퀀스가 0이 되면, 해당 밀리초 내에서 생성 가능한 ID를 모두 사용했으므로 다음 밀리초를 기다림
				currentTimeMillis = waitNextMillis(currentTimeMillis);
			}
		} else {
			// 새로운 밀리초에 도달하면 시퀀스를 0으로 초기화
			sequence = 0;
		}

		// 마지막 ID 생성 시간을 현재 시간으로 업데이트
		lastTimeMillis = currentTimeMillis;

		// ID를 구성하여 반환
		// - (현재 시간 - 시작 시간)를 왼쪽으로 (NODE_ID_BITS + SEQUENCE_BITS)만큼 이동 (타임스탬프 저장)
		// - 노드 ID를 왼쪽으로 SEQUENCE_BITS만큼 이동 (노드 ID 저장)
		// - 마지막으로 시퀀스 번호를 OR 연산하여 추가 (밀리초 내에서의 순서 정보 저장)
		return ((currentTimeMillis - startTimeMillis) << (NODE_ID_BITS + SEQUENCE_BITS))
			| (nodeId << SEQUENCE_BITS)
			| sequence;
	}

	// 다음 밀리초까지 대기하는 메서드 (ID 중복 방지를 위해 사용)
	private long waitNextMillis(long currentTimestamp) {
		// 새로운 밀리초가 될 때까지 루프를 돌며 대기
		while (currentTimestamp <= lastTimeMillis) {
			currentTimestamp = System.currentTimeMillis();
		}
		return currentTimestamp;
	}
}
