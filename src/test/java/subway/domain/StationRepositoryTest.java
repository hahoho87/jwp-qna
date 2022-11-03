package subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import qna.NotFoundException;

@DataJpaTest
class StationRepositoryTest {

	@Autowired
	private StationRepository stationRepository;

	@Test
	void save() {
		Station station = new Station("잠실역");
		Station savedStation = stationRepository.save(station);
		assertThat(savedStation.getId()).isNotNull();
	}

	@Test
	void findName() {
		// given
		String stationName = "잠실역";
		Station station = new Station(stationName);

		// when
		stationRepository.save(station);
		Station foundStation = stationRepository.findByName(stationName);

		// then
		assertThat(foundStation).isNotNull();
		assertThat(foundStation.getName()).isEqualTo(stationName);
	}

	@Test
	void identity() {
		final Station station1 = stationRepository.save(new Station("잠실역"));
		// final Station station2 = stationRepository.findByName("잠실역");
		// 영속성 컨텍스트에서 가지고 오기 때문에 select query 나가지 않음
		final Station station2 = stationRepository.findById(station1.getId())
			.orElseThrow(NotFoundException::new);
		assertThat(station1).isSameAs(station2);
	}

	@Test
	void dirtyChecking() {
		final Station station = stationRepository.save(new Station("잠실역"));
		station.setName("잠실새내역");
		final Station foundStation = stationRepository.findByName("잠실새내역");
		assertThat(foundStation.getName()).isEqualTo("잠실새내역");
	}
}