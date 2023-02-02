var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI')
var Opcodes = Java.type('org.objectweb.asm.Opcodes')
var InsnList = Java.type('org.objectweb.asm.tree.InsnList')
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode')
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode')
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode')

function initializeCoreMod() {
    return {
        'bovinesandbuttercups_load_bovinestate_models': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.resources.model.ModelBakery',
                'methodName': '<init>',
                'methodDesc': '(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;I)V'
            },
            'transformer': function(node) {
                var target = ASMAPI.findFirstMethodCallAfter(node, ASMAPI.MethodType.VIRTUAL, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapMethod("m_119306_"), "(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", 189); // loadTopLevel
                if (target === null) {
                    throw "Could not find loadTopLevel node."
                }
                var ls = new InsnList();
                ls.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
				// resourceManager
                ls.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                ls.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119243_"), "Lnet/minecraft/server/packs/resources/ResourceManager;"));
                // unbakedCache
                ls.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                ls.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119212_"), "Ljava/util/Map;"));
                // topLevelModels
                ls.add(new VarInsnNode(Opcodes.ALOAD, 0)); // this
                ls.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/model/ModelBakery", ASMAPI.mapField("f_119214_"), "Ljava/util/Map;"));

                ls.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/merchantpug/bovinesandbuttercups/client/util/BovineStateModelUtil", "initModels", "(Lnet/minecraft/client/resources/model/ModelBakery;Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/Map;Ljava/util/Map;)V", false));

                node.instructions.insert(target, ls);
                return node;
            }
        }
    }
}