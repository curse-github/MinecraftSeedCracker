/*    */ package net.minecraft.world.item.enchantment.effects;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.functions.CommandFunction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.item.enchantment.EnchantedItemInUse;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class RunFunction extends Record implements EnchantmentEntityEffect {
/* 20 */   public RunFunction(Identifier function) { this.function = function; } private final Identifier function; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/effects/RunFunction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 20 */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/RunFunction; } public Identifier function() { return this.function; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/effects/RunFunction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/enchantment/effects/RunFunction; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/effects/RunFunction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/enchantment/effects/RunFunction;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 25 */   public static final MapCodec<RunFunction> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 26 */         .fieldOf("function").forGetter(RunFunction::function))
/* 27 */       .apply(i, RunFunction::new));
/*    */ 
/*    */   
/*    */   public void apply(ServerLevel serverLevel, int enchantmentLevel, EnchantedItemInUse item, Entity entity, Vec3 position) {
/* 31 */     MinecraftServer server = serverLevel.getServer();
/* 32 */     ServerFunctionManager functions = server.getFunctions();
/* 33 */     Optional<CommandFunction<CommandSourceStack>> function = functions.get(this.function);
/* 34 */     if (function.isPresent()) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 41 */       CommandSourceStack source = server.createCommandSourceStack().withPermission(LevelBasedPermissionSet.GAMEMASTER).withSuppressedOutput().withEntity(entity).withLevel(serverLevel).withPosition(position).withRotation(entity.getRotationVector());
/* 42 */       functions.execute((CommandFunction)function.get(), source);
/*    */     } else {
/* 44 */       LOGGER.error("Enchantment run_function effect failed for non-existent function {}", this.function);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 50 */   public MapCodec<RunFunction> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\enchantment\effects\RunFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */