/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ import net.minecraft.server.permissions.PermissionLevel;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.ServerOpListEntry;
/*    */ 
/*    */ public final class OperatorDto extends Record {
/*    */   private final PlayerDto player;
/*    */   private final Optional<PermissionLevel> permissionLevel;
/*    */   private final Optional<Boolean> bypassesPlayerLimit;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto; }
/*    */   
/* 22 */   public OperatorDto(PlayerDto player, Optional<PermissionLevel> permissionLevel, Optional<Boolean> bypassesPlayerLimit) { this.player = player; this.permissionLevel = permissionLevel; this.bypassesPlayerLimit = bypassesPlayerLimit; } public PlayerDto player() { return this.player; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #22	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/OperatorService$OperatorDto;
/* 22 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<PermissionLevel> permissionLevel() { return this.permissionLevel; } public Optional<Boolean> bypassesPlayerLimit() { return this.bypassesPlayerLimit; }
/* 23 */   public static final MapCodec<OperatorDto> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(PlayerDto.CODEC
/* 24 */         .codec().fieldOf("player").forGetter(OperatorDto::player), PermissionLevel.INT_CODEC
/* 25 */         .optionalFieldOf("permissionLevel").forGetter(OperatorDto::permissionLevel), Codec.BOOL
/* 26 */         .optionalFieldOf("bypassesPlayerLimit").forGetter(OperatorDto::bypassesPlayerLimit))
/* 27 */       .apply(i, OperatorDto::new));
/*    */   
/*    */   public static OperatorDto from(ServerOpListEntry serverOpListEntry) {
/* 30 */     return new OperatorDto(PlayerDto.from(
/* 31 */           (NameAndId)Objects.requireNonNull((NameAndId)serverOpListEntry.getUser())), 
/* 32 */         Optional.of(serverOpListEntry.permissions().level()), 
/* 33 */         Optional.of(Boolean.valueOf(serverOpListEntry.getBypassesPlayerLimit())));
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\OperatorService$OperatorDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */