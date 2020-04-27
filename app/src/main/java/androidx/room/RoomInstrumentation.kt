package androidx.room

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodNode
import java.io.File

class RoomInstrumentation(
    private val tracingMethodsClassContent: ByteArray,
    private val loadClassNodeByName: (String) -> ClassNode
) {

  constructor(
      tracingMethodsClassContent: File,
      loadClassNodeByName: (String) -> ClassNode
  ) : this(
      tracingMethodsClassContent.readBytes(),
      loadClassNodeByName
  )

  fun instrument(
      sourceClass: ClassNode,
      daoName: String
  ) {
    val tracingMethods = readTracingMethodsFromClassFile(daoName)
    val daoMethods = sourceClass
        .methods
        .filter { !it.isConstructor }
        .filter { !it.isSyntheticAccessor }
        .filter { it.isPublic || it.isProtected }
    val reactiveMethods = daoMethods.filter(MethodNode::isRxQuery)
    val anonymousClassNodes = reactiveMethods
        .map { it.anonymousCallableClassName }
        .map(loadClassNodeByName)

    val callableMethodsToTransform = anonymousClassNodes
        .onEach { print(it.name) }
        .map { it.callableMethodForReturnType(it.callableReturnType) }
        .onEach { print(" --> ${it.name}\n") }

    daoMethods
  }

  private fun readTracingMethodsFromClassFile(
      daoName: String
  ): List<MethodNode> {
    return tracingMethodsClassContent
        .toClassNode()
        .methods
        .filter { !it.isConstructor }
        .onEach { methodNode ->
          // Add the name of the DAO class as an attribute the trace
          val node = methodNode.instructions.first { it is LdcInsnNode && it.cst == "dao" }.next as LdcInsnNode
          node.cst = daoName
        }
  }
}


