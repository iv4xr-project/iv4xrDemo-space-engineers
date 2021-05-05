package spaceEngineers.commands

import spaceEngineers.model.Vec2
import spaceEngineers.model.Vec3
import kotlin.jvm.JvmOverloads


class MovementArgs @JvmOverloads
constructor(val movement: Vec3 = Vec3.ZERO, val rotation3: Vec2 = Vec2.ZERO, val roll: Float = 0f)
