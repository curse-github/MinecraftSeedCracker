/*    */ package net.minecraft.network.chat.contents;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.selector.SelectorPattern;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentContents;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ScoreContents extends Record implements ComponentContents {
/*    */   private final Either<SelectorPattern, String> name;
/*    */   private final String objective;
/*    */   
/* 26 */   public ScoreContents(Either<SelectorPattern, String> name, String objective) { this.name = name; this.objective = objective; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/contents/ScoreContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 26 */     //   0	7	0	this	Lnet/minecraft/network/chat/contents/ScoreContents; } public Either<SelectorPattern, String> name() { return this.name; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/contents/ScoreContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #26	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/contents/ScoreContents;
/* 26 */     //   0	8	1	o	Ljava/lang/Object; } public String objective() { return this.objective; }
/* 27 */   public static final MapCodec<ScoreContents> INNER_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
/* 28 */         Codec.either(SelectorPattern.CODEC, Codec.STRING).fieldOf("name").forGetter(ScoreContents::name), Codec.STRING
/* 29 */         .fieldOf("objective").forGetter(ScoreContents::objective))
/* 30 */       .apply(i, ScoreContents::new));
/*    */   
/* 32 */   public static final MapCodec<ScoreContents> MAP_CODEC = INNER_CODEC.fieldOf("score");
/*    */ 
/*    */ 
/*    */   
/* 36 */   public MapCodec<ScoreContents> codec() { return MAP_CODEC; }
/*    */ 
/*    */   
/*    */   private ScoreHolder findTargetName(CommandSourceStack source) throws CommandSyntaxException {
/* 40 */     Optional<SelectorPattern> selector = this.name.left();
/* 41 */     if (selector.isPresent()) {
/* 42 */       List<? extends Entity> entities = ((SelectorPattern)selector.get()).resolved().findEntities(source);
/* 43 */       if (!entities.isEmpty()) {
/* 44 */         if (entities.size() != 1) {
/* 45 */           throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
/*    */         }
/* 47 */         return (ScoreHolder)entities.getFirst();
/*    */       } 
/* 49 */       return ScoreHolder.forNameOnly(((SelectorPattern)selector.get()).pattern());
/*    */     } 
/* 51 */     return ScoreHolder.forNameOnly((String)this.name.right().orElseThrow());
/*    */   }
/*    */   
/*    */   private MutableComponent getScore(ScoreHolder name, CommandSourceStack source) {
/* 55 */     MinecraftServer server = source.getServer();
/* 56 */     if (server != null) {
/* 57 */       ServerScoreboard serverScoreboard = server.getScoreboard();
/* 58 */       Objective objective = serverScoreboard.getObjective(this.objective);
/*    */       
/* 60 */       if (objective != null) {
/* 61 */         ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(name, objective);
/* 62 */         if (scoreInfo != null) {
/* 63 */           return scoreInfo.formatValue(objective.numberFormatOrDefault(StyledFormat.NO_STYLE));
/*    */         }
/*    */       } 
/*    */     } 
/* 67 */     return Component.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public MutableComponent resolve(CommandSourceStack source, Entity entity, int recursionDepth) throws CommandSyntaxException {
/* 72 */     if (source == null) {
/* 73 */       return Component.empty();
/*    */     }
/*    */     
/* 76 */     ScoreHolder scoreHolder = findTargetName(source);
/* 77 */     Entity entity1 = (entity != null && scoreHolder.equals(ScoreHolder.WILDCARD)) ? entity : scoreHolder;
/* 78 */     return getScore(entity1, source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 83 */   public String toString() { return "score{name='" + String.valueOf(this.name) + "', objective='" + this.objective + "'}"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\contents\ScoreContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */