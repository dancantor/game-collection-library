using GameLibraryAPI.Dto;
using GameLibraryAPI.Model;
using GameLibraryAPI.Repository;
using GameLibraryAPI.Validation;

namespace GameLibraryAPI.Service
{
    public interface IGameService
    {
        Task<List<GameDto>> GetAllGames();
        Task<GameDto> GetGameById(int id);
        Task<GameDto> AddNewGame(GameDto gameDto);
        Task<GameDto> UpdateGame(GameDto gameDto, int gameId);
        Task<GameDto> RemoveGame(int id);
    }
    public class GameService : IGameService
    {
        private readonly IGameRepository gameRepository;
        private readonly IMapper<GameLibrary, GameDto> gameMapper;

        public GameService(IGameRepository gameRepository, IMapper<GameLibrary, GameDto> mapper)
        {
            this.gameRepository = gameRepository;
            this.gameMapper = mapper;
        }

        public async Task<GameDto> AddNewGame(GameDto gameDto)
        {
            GameLibrary gameModel = gameMapper.MapDtoToModel(gameDto);
            return gameMapper.MapModelToDto(await this.gameRepository.AddNewGame(gameModel));
        }

        public async Task<List<GameDto>> GetAllGames()
        {
            return gameMapper.MapListModelToDto(await this.gameRepository.GetAllGames());
        }

        public async Task<GameDto> GetGameById(int id)
        {
            GameLibrary? game = await gameRepository.GetGameById(id);
            if (game == null)
            {
                throw new GameNotFoundException($"Game with id {id} was not found");
            }
            return gameMapper.MapModelToDto(game);
        }

        public async Task<GameDto> RemoveGame(int id)
        {
            GameLibrary? game = await gameRepository.RemoveGame(id);
            if (game == null)
            {
                throw new GameNotFoundException($"Game with id {id} was not found");
            }
            return gameMapper.MapModelToDto(game);
        }

        public async Task<GameDto> UpdateGame(GameDto gameDto, int gameId)
        {
            GameLibrary gameToUpdate = gameMapper.MapDtoToModel(gameDto);
            gameToUpdate.Id = gameId;
            GameLibrary? game = await gameRepository.UpdateGame(gameToUpdate);
            if (game == null)
            {
                throw new GameNotFoundException($"Game with id {gameId} was not found");
            }
            return gameMapper.MapModelToDto(game);
        }
    }
}
