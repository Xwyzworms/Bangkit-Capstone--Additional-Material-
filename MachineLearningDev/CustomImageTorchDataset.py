from torch.utils.data import Dataset
from PIL import Image
#%%
class CustomImageDataset(Dataset):
    def __init__(self, data_directory : str,
                  targets : str,
                  transform : None):
        self.data_dir = data_directory
        self.transform = transform
        self.targets = targets
    
    def __len__(self):
        return len(self.data_dir)

    def __getitem__(self, index) :
        concrete_image_path : str = self.data_dir[index]
        image = Image.open(concrete_image_path)
        target = self.targets[index]
        if(self.transform) :
            image = self.transform(image)
        return image, target

        