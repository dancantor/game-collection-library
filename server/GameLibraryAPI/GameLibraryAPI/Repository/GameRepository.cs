using GameLibraryAPI.Data;
using GameLibraryAPI.Model;
using Microsoft.EntityFrameworkCore;
using System.Runtime.CompilerServices;

namespace GameLibraryAPI.Repository
{
    public interface IGameRepository
    {
        Task<List<GameLibrary>> GetAllGames();
        Task<GameLibrary?> GetGameById(int id);
        Task<GameLibrary> AddNewGame(GameLibrary newGame);
        Task<GameLibrary?> RemoveGame(int gameId);
        Task<GameLibrary?> UpdateGame(GameLibrary gameToUpdate);
    }
    public class GameRepository : IGameRepository
    {
        private readonly gamelibraryContext _dbContext;

        public GameRepository(gamelibraryContext dbContext)
        {
            _dbContext = dbContext;
        }

        public async Task<GameLibrary> AddNewGame(GameLibrary newGame)
        {
            _dbContext.Add(newGame);
            await _dbContext.SaveChangesAsync();
            return newGame;
        }

        public async Task<List<GameLibrary>> GetAllGames()
        {
            return await _dbContext.GameLibraries.ToListAsync();
        }

        public async Task<GameLibrary?> GetGameById(int id)
        {
            return await _dbContext.GameLibraries.Where(game => game.Id == id).FirstOrDefaultAsync();
        }

        public async Task<GameLibrary?> RemoveGame(int gameId)
        {
            GameLibrary? gameToDelete = await _dbContext.GameLibraries.FindAsync(gameId);
            if (gameToDelete == null)
            {
                return null;
            }
            _dbContext.GameLibraries.Remove(gameToDelete);
            await _dbContext.SaveChangesAsync();
            return gameToDelete;
        }

        public async Task<GameLibrary?> UpdateGame(GameLibrary gameToUpdate)
        {
            GameLibrary? gameFromDb = await _dbContext.GameLibraries.Where(game => game.Id == gameToUpdate.Id).FirstOrDefaultAsync();
            if (gameFromDb == null)
            {
                return null;
            }
            GameLibrary oldGameCopy = new GameLibrary
            {
                Id = gameFromDb.Id,
                Name = gameFromDb.Name,
                Producer = gameFromDb.Producer,
                PurchaseDate = gameFromDb.PurchaseDate,
                LastTimePlayed = gameFromDb.LastTimePlayed,
                Price = gameFromDb.Price,
                Currency = gameFromDb.Currency,
                FormatOfGame = gameFromDb.FormatOfGame,
                Platform = gameFromDb.Platform,
                PicturePath = gameFromDb.PicturePath
            };
            gameFromDb.Name = gameToUpdate.Name;
            gameFromDb.Producer = gameToUpdate.Producer;
            gameFromDb.PurchaseDate = gameToUpdate.PurchaseDate;
            gameFromDb.LastTimePlayed = gameToUpdate.LastTimePlayed;
            gameFromDb.Price = gameToUpdate.Price;
            gameFromDb.Currency = gameToUpdate.Currency;
            gameFromDb.FormatOfGame = gameToUpdate.FormatOfGame;
            gameFromDb.Platform = gameToUpdate.Platform;
            gameFromDb.PicturePath = gameToUpdate.PicturePath;
            await _dbContext.SaveChangesAsync();
            return oldGameCopy;
        }
    }
}
