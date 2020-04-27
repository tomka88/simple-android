package androidx.room

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TypeInsnNode
import java.io.File
import java.io.InputStream

fun File.toClassNode(): ClassNode {
  val classReader = ClassReader(readBytes())

  return ClassNode().apply {
    classReader.accept(this, 0)
  }
}

fun InputStream.toClassNode(): ClassNode {
  val classReader = ClassReader(this)

  return ClassNode().apply {
    classReader.accept(this, 0)
  }
}

fun ByteArray.toClassNode(): ClassNode {
  val classReader = ClassReader(this)

  return ClassNode().apply {
    classReader.accept(this, 0)
  }
}

val MethodNode.isConstructor: Boolean
  get() = name == "<init>"

val MethodNode.isPublic: Boolean
  get() = access and Opcodes.ACC_PUBLIC == Opcodes.ACC_PUBLIC

val MethodNode.isProtected: Boolean
  get() = access and Opcodes.ACC_PROTECTED == Opcodes.ACC_PROTECTED

val MethodNode.isSyntheticAccessor: Boolean
  get() = name.startsWith("access$")

val MethodNode.isRxQuery: Boolean
  get() = desc.contains(")Lio/reactivex/")

val MethodNode.anonymousCallableClassName: String
  get() {
    val node = instructions
        .first { it is TypeInsnNode && it.desc == "java/util/concurrent/Callable" && (it.previous is MethodInsnNode || it.previous is FieldInsnNode) }
        .previous

    return when {
      node is MethodInsnNode && node.name == "<init>" -> "${node.owner.substringAfterLast('/')}.class"
      node is FieldInsnNode && node.name == "INSTANCE" -> "${node.owner.substringAfterLast('/')}.class"
      else -> throw RuntimeException("Unknown reactive query in Room for tracing!")
    }
  }

data class InsnInfo(
    val opCode: Int,
    val desc: String,
    val type: String
) {

  companion object {
    fun from(instructions: InsnList): List<InsnInfo> = instructions.map(::InsnInfo)
  }

  constructor(node: AbstractInsnNode) : this(
      opCode = node.opcode,
      type = node::class.java.simpleName,
      desc = ""
  )
}

val ClassNode.callableReturnType: String
  get() = signature.substring(
      startIndex = signature.lastIndexOf("<L") + 1,
      endIndex = signature.lastIndexOf(";>")
  )

fun ClassNode.callableMethodForReturnType(returnType: String): MethodNode {
  val expectedMethodDesc = when {
    returnType == "Ljava/lang/Object" -> "()V"
    returnType.startsWith("Ljava/util/List<") -> "()Ljava/util/List;"
    else -> "()$returnType;"
  }
  return methods.first { it.name == "call" && it.desc == expectedMethodDesc }
}
