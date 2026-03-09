/*    */ package net.minecraft.world.scores;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.numbers.NumberFormat;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ReadOnlyScoreInfo
/*    */ {
/*    */   int value();
/*    */   
/*    */   boolean isLocked();
/*    */   
/*    */   NumberFormat numberFormat();
/*    */   
/* 17 */   default MutableComponent formatValue(NumberFormat defaultFormat) { return ((NumberFormat)Objects.requireNonNullElse(numberFormat(), defaultFormat)).format(value()); }
/*    */ 
/*    */ 
/*    */   
/* 21 */   static MutableComponent safeFormatValue(ReadOnlyScoreInfo scoreInfo, NumberFormat defaultFormat) { return (scoreInfo != null) ? scoreInfo.formatValue(defaultFormat) : defaultFormat.format(0); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\scores\ReadOnlyScoreInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */