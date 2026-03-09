/*    */ package net.minecraft.server.commands.data;
/*    */ 
/*    */ import com.mojang.brigadier.builder.ArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Locale;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.storage.TagValueInput;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockDataAccessor
/*    */   implements DataAccessor
/*    */ {
/* 31 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 33 */   private static final SimpleCommandExceptionType ERROR_NOT_A_BLOCK_ENTITY = new SimpleCommandExceptionType(Component.translatable("commands.data.block.invalid"));
/*    */   
/* 35 */   public static final Function<String, DataCommands.DataProvider> PROVIDER = argPrefix -> new DataCommands.DataProvider()
/*    */     {
/*    */       public DataAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 38 */         BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, argPrefix + "Pos");
/* 39 */         BlockEntity entity = ((CommandSourceStack)context.getSource()).getLevel().getBlockEntity(pos);
/* 40 */         if (entity == null) {
/* 41 */           throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
/*    */         }
/* 43 */         return new BlockDataAccessor(entity, pos);
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 48 */       public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> parent, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> function) { return parent.then(Commands.literal("block").then((ArgumentBuilder)function.apply(Commands.argument(argPrefix + "Pos", BlockPosArgument.blockPos())))); }
/*    */     };
/*    */ 
/*    */   
/*    */   private final BlockEntity entity;
/*    */   private final BlockPos pos;
/*    */   
/*    */   public BlockDataAccessor(BlockEntity entity, BlockPos pos) {
/* 56 */     this.entity = entity;
/* 57 */     this.pos = pos;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setData(CompoundTag tag) {
/* 62 */     BlockState state = this.entity.getLevel().getBlockState(this.pos);
/* 63 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(this.entity.problemPath(), LOGGER); 
/* 64 */     try { this.entity.loadWithComponents(TagValueInput.create(reporter, this.entity.getLevel().registryAccess(), tag));
/*    */       
/* 66 */       this.entity.setChanged();
/* 67 */       this.entity.getLevel().sendBlockUpdated(this.pos, state, state, 3);
/* 68 */       reporter.close(); }
/*    */     catch (Throwable throwable) { try { reporter.close(); }
/*    */       catch (Throwable throwable1)
/*    */       { throwable.addSuppressed(throwable1); }
/*    */        throw throwable; }
/* 73 */      } public CompoundTag getData() { return this.entity.saveWithFullMetadata(this.entity.getLevel().registryAccess()); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 78 */   public Component getModifiedSuccess() { return Component.translatable("commands.data.block.modified", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()) }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 83 */   public Component getPrintSuccess(Tag data) { return Component.translatable("commands.data.block.query", new Object[] { Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), NbtUtils.toPrettyComponent(data) }); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 88 */   public Component getPrintSuccess(NbtPathArgument.NbtPath path, double scale, int value) { return Component.translatable("commands.data.block.get", new Object[] { path.asString(), Integer.valueOf(this.pos.getX()), Integer.valueOf(this.pos.getY()), Integer.valueOf(this.pos.getZ()), String.format(Locale.ROOT, "%.2f", new Object[] { Double.valueOf(scale) }), Integer.valueOf(value) }); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\data\BlockDataAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */