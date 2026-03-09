/*    */ package net.minecraft.world.attribute;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class BedRule extends Record {
/*    */   private final Rule canSleep;
/*    */   private final Rule canSetSpawn;
/*    */   private final boolean explodes;
/*    */   private final Optional<Component> errorMessage;
/*    */   
/* 13 */   public BedRule(Rule canSleep, Rule canSetSpawn, boolean explodes, Optional<Component> errorMessage) { this.canSleep = canSleep; this.canSetSpawn = canSetSpawn; this.explodes = explodes; this.errorMessage = errorMessage; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/BedRule;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule; } public Rule canSleep() { return this.canSleep; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/BedRule;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/attribute/BedRule; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/BedRule;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/attribute/BedRule;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } public Rule canSetSpawn() { return this.canSetSpawn; } public boolean explodes() { return this.explodes; } public Optional<Component> errorMessage() { return this.errorMessage; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final BedRule CAN_SLEEP_WHEN_DARK = new BedRule(Rule.WHEN_DARK, Rule.ALWAYS, false, 
/*    */ 
/*    */ 
/*    */       
/* 23 */       Optional.of(Component.translatable("block.minecraft.bed.no_sleep")));
/*    */   
/* 25 */   public static final BedRule EXPLODES = new BedRule(Rule.NEVER, Rule.NEVER, true, Optional.empty());
/*    */   
/* 27 */   public static final Codec<BedRule> CODEC = RecordCodecBuilder.create(i -> i.group(Rule.CODEC
/* 28 */         .fieldOf("can_sleep").forGetter(BedRule::canSleep), Rule.CODEC
/* 29 */         .fieldOf("can_set_spawn").forGetter(BedRule::canSetSpawn), Codec.BOOL
/* 30 */         .optionalFieldOf("explodes", Boolean.valueOf(false)).forGetter(BedRule::explodes), ComponentSerialization.CODEC
/* 31 */         .optionalFieldOf("error_message").forGetter(BedRule::errorMessage))
/* 32 */       .apply(i, BedRule::new));
/*    */ 
/*    */   
/* 35 */   public boolean canSleep(Level level) { return this.canSleep.test(level); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public boolean canSetSpawn(Level level) { return this.canSetSpawn.test(level); }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public Player.BedSleepingProblem asProblem() { return new Player.BedSleepingProblem((Component)this.errorMessage.orElse(null)); }
/*    */   
/*    */   public enum Rule
/*    */     implements StringRepresentable {
/* 47 */     ALWAYS("always"),
/*    */ 
/*    */     
/* 50 */     WHEN_DARK("when_dark"),
/* 51 */     NEVER("never"); public static final Codec<Rule> CODEC; private final String name;
/*    */     
/*    */     static  {
/* 54 */       CODEC = StringRepresentable.fromEnum(Rule::values);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 59 */     Rule(String name) { this.name = name; }
/*    */ 
/*    */     
/*    */     public boolean test(Level level) {
/* 63 */       switch (ordinal()) { default: throw new MatchException(null, null);case 0: case 1: case 2: break; }  return false;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     public String getSerializedName() { return this.name; }
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\BedRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */