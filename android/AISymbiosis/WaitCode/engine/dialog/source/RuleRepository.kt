package cn.darkfog.dialog_manager.data.source

import cn.darkfog.dialog_manager.model.bean.ASRRule
import cn.darkfog.dialog_manager.model.bean.NLURule
import cn.darkfog.foundation.util.StorageUtil
import io.reactivex.Completable
import java.util.*

object RuleRepository {


    fun init(): Completable {
        return PrivateRuleRepository.loadASRRules().andThen(PrivateRuleRepository.loadNLURules())
    }

    fun getASRRule(asr: ASR): ASRRule? {
        return PrivateRuleRepository.asrRules[asr.content]
    }

    fun addASRRule(rule: ASRRule): Completable {
        PrivateRuleRepository.asrRules[rule.originAsr.content] = rule
        return PrivateRuleRepository.saveASRRules()
    }

    fun deleteASRRule(rule: ASRRule): Completable {
        PrivateRuleRepository.asrRules.remove(rule.originAsr.content)
        return PrivateRuleRepository.saveASRRules()
    }

    fun getNLURule(asr: ASR): NLURule? {
        return PrivateRuleRepository.nluRules[asr.content]
    }

    fun addNLURule(rule: NLURule): Completable {
        PrivateRuleRepository.nluRules[rule.asr.content] = rule
        return PrivateRuleRepository.saveNLURules()
    }

    fun deleteNLURule(rule: NLURule): Completable {
        PrivateRuleRepository.nluRules.remove(rule.asr.content, rule)
        return PrivateRuleRepository.saveNLURules()
    }

    private object PrivateRuleRepository {

        const val ASR_RULE_SP = "ASR_RULE_SP"
        const val NLU_RULE_SP = "NLU_RULE_SP"

        val asrRules = HashMap<String, ASRRule>()
        val nluRules = HashMap<String, NLURule>()

        fun loadASRRules(): Completable {
            return StorageUtil.getCache(HashMap::class.java, ASR_RULE_SP)
                .doOnSuccess {
                    asrRules.putAll(it as HashMap<String, ASRRule>)
                }.doOnError {

                }.ignoreElement()
        }

        fun saveASRRules(): Completable {
            return StorageUtil.saveCache(asrRules, ASR_RULE_SP)
        }

        fun loadNLURules(): Completable {
            return StorageUtil.getCache(HashMap::class.java, NLU_RULE_SP)
                .doOnSuccess {
                    nluRules.putAll(it as HashMap<String, NLURule>)
                }.doOnError {

                }.ignoreElement()
        }

        fun saveNLURules(): Completable {
            return StorageUtil.saveCache(nluRules, ASR_RULE_SP)
        }

    }
}
