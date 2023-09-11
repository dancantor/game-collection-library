using GameLibraryAPI.Model;

namespace GameLibraryAPI.Dto
{
    public interface IMapper<Model, Dto>
    {
        public Dto MapModelToDto(Model model);
        public Model MapDtoToModel(Dto dto);
        public List<Dto> MapListModelToDto(List<Model> models);
        public List<Model> MapListDtoToModel(List<Dto> dtos);
    }
    public class GameMapper : IMapper<GameLibrary, GameDto>
    {

        public GameDto MapModelToDto(GameLibrary game)
        {
            return new GameDto
            {
                Id = game.Id,
                Name = game.Name,
                Producer = game.Producer,
                PurchaseDate = game.PurchaseDate,
                LastTimePlayed = game.LastTimePlayed,
                Price = game.Price,
                Currency = (CurrencyType?)Enum.Parse(typeof(CurrencyType), game.Currency),
                FormatOfGame = (FormatType?)Enum.Parse(typeof(FormatType), game.FormatOfGame),
                Platform = (PlatformType?)Enum.Parse(typeof(PlatformType), game.Platform),
                PicturePath = game.PicturePath
            };
        }

        public GameLibrary MapDtoToModel(GameDto game)
        {
            return new GameLibrary
            {
                Id = 0,
                Name = game.Name,
                Producer = game.Producer,
                PurchaseDate = game.PurchaseDate,
                LastTimePlayed = game.LastTimePlayed,
                Price = game.Price,
                Currency = game.Currency.ToString(),
                FormatOfGame = game.FormatOfGame.ToString(),
                Platform = game.Platform.ToString(),
                PicturePath = game.PicturePath
            };
        }

        public List<GameLibrary> MapListDtoToModel(List<GameDto> games)
        {
            return games.Select(game => MapDtoToModel(game)).ToList();
        }

        public List<GameDto> MapListModelToDto(List<GameLibrary> games)
        {
            return games.Select(game => MapModelToDto(game)).ToList();
        }
    }
}
