using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;
using GameLibraryAPI.Model;

namespace GameLibraryAPI.Data
{
    public partial class gamelibraryContext : DbContext
    {
        public gamelibraryContext()
        {
        }

        public gamelibraryContext(DbContextOptions<gamelibraryContext> options)
            : base(options)
        {
        }

        public virtual DbSet<GameLibrary> GameLibraries { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
#warning To protect potentially sensitive information in your connection string, you should move it out of source code. You can avoid scaffolding the connection string by using the Name= syntax to read it from configuration - see https://go.microsoft.com/fwlink/?linkid=2131148. For more guidance on storing connection strings, see http://go.microsoft.com/fwlink/?LinkId=723263.
                optionsBuilder.UseSqlServer("Data Source=DESKTOP-J8IUEU9;Initial Catalog=game-library;Integrated Security=True");
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<GameLibrary>(entity =>
            {
                entity.ToTable("GameLibrary");

                entity.Property(e => e.Id).HasColumnName("id");

                entity.Property(e => e.Currency)
                    .HasMaxLength(15)
                    .IsUnicode(false)
                    .HasColumnName("currency");

                entity.Property(e => e.FormatOfGame)
                    .HasMaxLength(15)
                    .IsUnicode(false)
                    .HasColumnName("formatOfGame");

                entity.Property(e => e.LastTimePlayed)
                    .HasMaxLength(18)
                    .IsUnicode(false)
                    .HasColumnName("lastTimePlayed");

                entity.Property(e => e.Name)
                    .HasMaxLength(63)
                    .IsUnicode(false)
                    .HasColumnName("name");

                entity.Property(e => e.PicturePath)
                    .HasMaxLength(255)
                    .IsUnicode(false)
                    .HasColumnName("picturePath");

                entity.Property(e => e.Platform)
                    .HasMaxLength(7)
                    .IsUnicode(false)
                    .HasColumnName("platform");

                entity.Property(e => e.Price).HasColumnName("price");

                entity.Property(e => e.Producer)
                    .HasMaxLength(31)
                    .IsUnicode(false)
                    .HasColumnName("producer");

                entity.Property(e => e.PurchaseDate)
                    .HasMaxLength(11)
                    .IsUnicode(false)
                    .HasColumnName("purchaseDate");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
