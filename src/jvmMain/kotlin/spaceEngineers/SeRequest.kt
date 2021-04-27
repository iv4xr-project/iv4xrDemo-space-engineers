package spaceEngineers

import communication.system.RequestType
import spaceEngineers.commands.SeAgentCommand
import spaceEngineers.commands.SeSessionCommand
import spaceEngineers.model.Observation
import kotlin.jvm.java

class SeRequest<ResponseType> @JvmOverloads constructor(
    @Transient
    val responseType: Class<ResponseType>,
    val cmd: RequestType,
    val arg: Any? = null
) {


    companion object {

        /**
         * Request an observation after executing the sent Command
         */
        fun command(command: SeAgentCommand): SeRequest<Observation> {
            return SeRequest(Observation::class.java, RequestType.AGENTCOMMAND, command)
        }

        fun session(sessionCommand: SeSessionCommand): SeRequest<Boolean> {
            return SeRequest(Boolean::class.java, RequestType.SESSION, sessionCommand)
        }
    }
}
