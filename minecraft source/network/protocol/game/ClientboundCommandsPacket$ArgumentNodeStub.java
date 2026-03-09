/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*     */ import net.minecraft.commands.CommandBuildContext;
/*     */ import net.minecraft.commands.synchronization.ArgumentTypeInfo;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ArgumentNodeStub
/*     */   extends Record
/*     */   implements ClientboundCommandsPacket.NodeStub
/*     */ {
/*     */   private final String id;
/*     */   private final ArgumentTypeInfo.Template<?> argumentType;
/*     */   private final Identifier suggestionId;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #151	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #151	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub; }
/*     */   
/*     */   public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #151	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket$ArgumentNodeStub;
/*     */     //   0	8	1	o	Ljava/lang/Object; }
/*     */   
/* 151 */   private ArgumentNodeStub(String id, ArgumentTypeInfo.Template<?> argumentType, Identifier suggestionId) { this.id = id; this.argumentType = argumentType; this.suggestionId = suggestionId; } public String id() { return this.id; } public ArgumentTypeInfo.Template<?> argumentType() { return this.argumentType; } public Identifier suggestionId() { return this.suggestionId; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <S> ArgumentBuilder<S, ?> build(CommandBuildContext context, ClientboundCommandsPacket.NodeBuilder<S> builder) {
/* 158 */     ArgumentType<?> type = this.argumentType.instantiate(context);
/* 159 */     return builder.createArgument(this.id, type, this.suggestionId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(FriendlyByteBuf output) {
/* 164 */     output.writeUtf(this.id);
/* 165 */     serializeCap(output, this.argumentType);
/* 166 */     if (this.suggestionId != null) {
/* 167 */       output.writeIdentifier(this.suggestionId);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 172 */   private static <A extends ArgumentType<?>> void serializeCap(FriendlyByteBuf output, ArgumentTypeInfo.Template<A> argumentType) { serializeCap(output, argumentType.type(), argumentType); }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeCap(FriendlyByteBuf output, ArgumentTypeInfo<A, T> info, ArgumentTypeInfo.Template<A> argumentType) {
/* 177 */     output.writeVarInt(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getId(info));
/* 178 */     info.serializeToNetwork(argumentType, output);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\game\ClientboundCommandsPacket$ArgumentNodeStub.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */