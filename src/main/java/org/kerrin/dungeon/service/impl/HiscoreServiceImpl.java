package org.kerrin.dungeon.service.impl;

import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.model.Account;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.repository.HiscoreRepo;
import org.kerrin.dungeon.service.HiscoreService;
import org.kerrin.dungeon.service.ServiceHelppers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HiscoreServiceImpl extends ServiceHelppers implements HiscoreService {
	private static final Logger logger = LoggerFactory.getLogger(HiscoreServiceImpl.class);

	private final HiscoreRepo hiscoreRepo;
	
	@Autowired	
    public HiscoreServiceImpl(HiscoreRepo hiscoreRepo) {
		super();
		this.hiscoreRepo = hiscoreRepo;
	}

	@Override
	@Transactional
    public Hiscore highestLevel(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.HIGHEST_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.HIGHEST_LEVEL, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}
	
	@Override
	@Transactional
    public Hiscore highestLevel(Account account, boolean hardcore, boolean ironborn, int level) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.HIGHEST_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.HIGHEST_LEVEL, hardcore, ironborn, new Date(), level);
		} else {
			hiscore.setScore(level);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore totalLevel(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOTAL_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOTAL_LEVEL, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore totalLevel(Account account, boolean hardcore, boolean ironborn, int level) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOTAL_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOTAL_LEVEL, hardcore, ironborn, new Date(), level);
		} else {
			hiscore.setScore(level);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore fastestMaxLevel(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.FASTEST_MAX_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.FASTEST_MAX_LEVEL, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore fastestMaxLevel(Account account, boolean hardcore, boolean ironborn, long duration) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.FASTEST_MAX_LEVEL);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.FASTEST_MAX_LEVEL, hardcore, ironborn, new Date(), duration);
		} else {
			hiscore.setScore(duration);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore fastestMaxLevelAfterReset(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.FASTEST_MAX_LEVEL_AFTER_RESET);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.FASTEST_MAX_LEVEL_AFTER_RESET, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore fastestMaxLevelAfterReset(Account account, boolean hardcore, boolean ironborn, long duration) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.FASTEST_MAX_LEVEL_AFTER_RESET);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.FASTEST_MAX_LEVEL_AFTER_RESET, hardcore, ironborn, new Date(), duration);
		} else {
			hiscore.setScore(duration);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore tokensEarnt(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOKENS_EARNT);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOKENS_EARNT, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore tokensEarnt(Account account, boolean hardcore, boolean ironborn, int earnt) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOKENS_EARNT);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOKENS_EARNT, hardcore, ironborn, new Date(), earnt);
		} else {
			hiscore.setScore(hiscore.getScore() + earnt);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore tokensPurchased(Account account, boolean hardcore, boolean ironborn) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOKENS_PURCHASED);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOKENS_PURCHASED, hardcore, ironborn, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
    public Hiscore tokensPurchased(Account account, boolean hardcore, boolean ironborn, int purchased) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndHardcoreAndIronbornAndType(
				account.getId(), hardcore, ironborn, HiscoreType.TOKENS_PURCHASED);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.TOKENS_PURCHASED, hardcore, ironborn, new Date(), purchased);
		} else {
			hiscore.setScore(hiscore.getScore() + purchased);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
	public Hiscore achievement(Account account) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndType(
				account.getId(), HiscoreType.ACHIEVEMENT_POINTS);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.ACHIEVEMENT_POINTS, null, null, new Date(), 0);
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
	@Transactional
	public Hiscore achievement(Account account, int points) {
		Hiscore hiscore = hiscoreRepo.getOneByAccountIdAndType(
				account.getId(), HiscoreType.ACHIEVEMENT_POINTS);
		if(hiscore == null) {
			hiscore = new Hiscore(-1, account, HiscoreType.ACHIEVEMENT_POINTS, null, null, new Date(), 0);
		} else {
			hiscore.setScore(hiscore.getScore() + points);
			hiscore.setTimestamp(new Date());
		}
		loadHiscoreLinkedTables(hiscore);
		hiscoreRepo.save(hiscore);
		return hiscore;
	}

	@Override
    @Transactional
    public Hiscore create(Hiscore hiscore) {
    	Hiscore createdHiscore = hiscore;
    	
        return hiscoreRepo.save(createdHiscore);
    }

	@Override
    @Transactional
	public Hiscore findById(long hiscoreId) {
		Hiscore hiscore = hiscoreRepo.findOne(hiscoreId);

		loadHiscoreLinkedTables(hiscore);
        return hiscore;
	}
    
    @Override
    @Transactional
    public List<Hiscore> findAllByAccount(Account account) {
    	List<Hiscore> hiscores = hiscoreRepo.findAllByAccountIdOrderByTypeAscIronbornAscHardcoreAsc(account.getId());
    	for(Hiscore hiscore:hiscores) {
    		loadHiscoreLinkedTables(hiscore);
    	}
        return hiscores;
    }

    @Override
    @Transactional
    public Hiscore update(Hiscore hiscore) {
        Hiscore updatedHiscore = hiscoreRepo.findById(hiscore.getId());
         
        if (updatedHiscore == null) {
        	logger.info("Hiscore {} was missing during update, creating", hiscore);
        	updatedHiscore = new Hiscore(-1,
        			hiscore.getAccount(), hiscore.getType(), hiscore.getHardcore(), hiscore.getIronborn(),
        			new Date(), hiscore.getScore());
        	updatedHiscore = hiscoreRepo.save(updatedHiscore);
        }
        
        updatedHiscore.setTimestamp(new Date());
        updatedHiscore.setScore(hiscore.getScore());
        updatedHiscore.setRank(hiscore.getRank());

		loadHiscoreLinkedTables(updatedHiscore);
        updatedHiscore = hiscoreRepo.save(updatedHiscore);
        return updatedHiscore;
    }

	@Override
	@Transactional
	public List<Hiscore> findAllByTypeAndHardcoreAndIronborn(
			HiscoreType type, boolean hardcore, boolean ironborn, Date sinceDate, int offset, int limit) {
		int page = offset / limit;
		Pageable pagable = new PageRequest(page, limit);
		List<Hiscore> hiscores = hiscoreRepo.findAllByTypeAndHardcoreAndIronbornAndTimestampGreaterThanOrderByScoreDesc(
				type, hardcore, ironborn, sinceDate, pagable);

		for(Hiscore hiscore:hiscores) {
    		loadHiscoreLinkedTables(hiscore);
    	}
        
		return hiscores;
	}

	@Override
	@Transactional
	public void unrankOutdatedScores(boolean hardcore, boolean ironborn, Date sinceDate) {
		hiscoreRepo.unrankOutdatedScores(hardcore, ironborn, sinceDate);
	}
}
